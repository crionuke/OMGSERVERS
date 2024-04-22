package com.omgservers.service.handler.internal;

import com.omgservers.model.dto.jenkins.GetLuaJitWorkerBuilderV1Request;
import com.omgservers.model.dto.jenkins.GetLuaJitWorkerBuilderV1Response;
import com.omgservers.model.dto.system.SyncEventRequest;
import com.omgservers.model.dto.system.SyncEventResponse;
import com.omgservers.model.dto.tenant.versionImageRef.SyncVersionImageRefRequest;
import com.omgservers.model.dto.tenant.versionImageRef.SyncVersionImageRefResponse;
import com.omgservers.model.dto.tenant.versionJenkinsRequest.ViewVersionJenkinsRequestsRequest;
import com.omgservers.model.dto.tenant.versionJenkinsRequest.ViewVersionJenkinsRequestsResponse;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.event.body.internal.VersionBuildingCheckingRequestedEventBodyModel;
import com.omgservers.model.event.body.internal.VersionBuildingFailedEventBodyModel;
import com.omgservers.model.event.body.internal.VersionBuildingFinishedEventBodyModel;
import com.omgservers.model.versionImageRef.VersionImageRefQualifierEnum;
import com.omgservers.model.versionJenkinsRequest.VersionJenkinsRequestModel;
import com.omgservers.service.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideBadRequestException;
import com.omgservers.service.exception.ServerSideBaseException;
import com.omgservers.service.factory.system.EventModelFactory;
import com.omgservers.service.factory.tenant.VersionImageRefModelFactory;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.integration.jenkins.JenkinsIntegration;
import com.omgservers.service.module.system.SystemModule;
import com.omgservers.service.module.tenant.TenantModule;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class VersionBuildingCheckingRequestedEventHandlerImpl implements EventHandler {

    final JenkinsIntegration jenkinsIntegration;
    final TenantModule tenantModule;
    final SystemModule systemModule;

    final VersionImageRefModelFactory versionImageRefModelFactory;

    final EventModelFactory eventModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.VERSION_BUILDING_CHECKING_REQUESTED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.debug("Handle event, {}", event);

        final var body = (VersionBuildingCheckingRequestedEventBodyModel) event.getBody();
        final var tenantId = body.getTenantId();
        final var versionId = body.getVersionId();
        final var checkingInterval = body.getCheckingInterval();

        final var idempotencyKey = event.getIdempotencyKey();

        return viewVersionJenkinsRequests(tenantId, versionId)
                .flatMap(versionJenkinsRequests -> {
                    if (!versionJenkinsRequests.isEmpty()) {
                        return Multi.createFrom().iterable(versionJenkinsRequests)
                                .onItem().transformToUniAndMerge(versionJenkinsRequest ->
                                        checkVersionJenkinsRequest(versionJenkinsRequest, idempotencyKey))
                                .collect().asList()
                                .flatMap(results -> {
                                    final var anyFailed =
                                            results.stream().anyMatch(JenkinsRequestResultEnum.FAILED::equals);
                                    if (anyFailed) {
                                        return syncVersionBuildingFailed(tenantId, versionId, idempotencyKey);
                                    }

                                    final var inProgress = results.stream()
                                            .anyMatch(JenkinsRequestResultEnum.IN_PROGRESS::equals);
                                    if (inProgress) {
                                        final var nextCheckingIn = checkingInterval * 2;
                                        return requestFurtherChecking(tenantId, versionId, nextCheckingIn,
                                                idempotencyKey);
                                    } else {
                                        return syncVersionBuildingFinished(tenantId, versionId, idempotencyKey);
                                    }
                                });
                    } else {
                        log.warn("Version jenkins requests was not found, skip operation, " +
                                "version={}/{}", tenantId, versionId);
                        return Uni.createFrom().voidItem();
                    }
                })
                .replaceWithVoid();
    }

    Uni<List<VersionJenkinsRequestModel>> viewVersionJenkinsRequests(final Long tenantId,
                                                                     final Long versionId) {
        final var request = new ViewVersionJenkinsRequestsRequest(tenantId, versionId);
        return tenantModule.getVersionService().viewVersionJenkinsRequests(request)
                .map(ViewVersionJenkinsRequestsResponse::getVersionJenkinsRequests);
    }

    Uni<JenkinsRequestResultEnum> checkVersionJenkinsRequest(
            final VersionJenkinsRequestModel versionJenkinsRequest,
            final String idempotencyKey) {
        final var tenantId = versionJenkinsRequest.getTenantId();
        final var versionId = versionJenkinsRequest.getVersionId();
        final var qualifier = versionJenkinsRequest.getQualifier();
        final var buildNumber = versionJenkinsRequest.getBuildNumber();
        log.info(
                "Checking jenkins request, versionJenkinsRequest={}/{}, versionId={}, qualifier={}, buildNumber={}",
                tenantId,
                versionJenkinsRequest.getId(),
                versionId,
                qualifier,
                buildNumber);

        return getLuaJitWorkerBuilderV1Request(buildNumber)
                .flatMap(imageId -> syncVersionImageRef(versionJenkinsRequest, imageId, idempotencyKey))
                .map(created -> JenkinsRequestResultEnum.FINISHED)
                .onFailure(ServerSideBadRequestException.class)
                .recoverWithUni(t -> {
                    if (t instanceof final ServerSideBaseException exception) {
                        if (exception.getQualifier().equals(ExceptionQualifierEnum.JENKINS_JOB_UNFINISHED)) {
                            log.info("Jenkins job is still in progress, object={}, {}", versionJenkinsRequest,
                                    t.getMessage());
                            return Uni.createFrom().item(JenkinsRequestResultEnum.IN_PROGRESS);
                        }
                    }

                    log.warn("Jenkins job failed, {}", t.getMessage());
                    return Uni.createFrom().item(JenkinsRequestResultEnum.FAILED);
                });
    }

    Uni<String> getLuaJitWorkerBuilderV1Request(final Integer buildNumber) {
        final var getLuaJitWorkerBuilderV1Request = new GetLuaJitWorkerBuilderV1Request(buildNumber);
        return jenkinsIntegration.getJenkinsService().getLuaJitWorkerBuilderV1(getLuaJitWorkerBuilderV1Request)
                .map(GetLuaJitWorkerBuilderV1Response::getImageId);
    }

    Uni<Boolean> syncVersionImageRef(final VersionJenkinsRequestModel versionJenkinsRequest,
                                     final String imageId,
                                     final String idempotencyKey) {

        final var tenantId = versionJenkinsRequest.getTenantId();
        final var versionId = versionJenkinsRequest.getVersionId();

        final var versionImageRef = versionImageRefModelFactory.create(tenantId,
                versionId,
                VersionImageRefQualifierEnum.UNIVERSAL,
                imageId,
                idempotencyKey);

        final var request = new SyncVersionImageRefRequest(versionImageRef);
        return tenantModule.getVersionService().syncVersionImageRefWithIdempotency(request)
                .map(SyncVersionImageRefResponse::getCreated);
    }

    Uni<Boolean> requestFurtherChecking(final Long tenantId,
                                        final Long versionId,
                                        final Integer checkingInterval,
                                        final String idempotencyKey) {
        final var eventBody = new VersionBuildingCheckingRequestedEventBodyModel(tenantId,
                versionId,
                checkingInterval);
        final var eventModel = eventModelFactory.create(eventBody,
                idempotencyKey + "/" + eventBody.getQualifier());
        eventModel.setDelayed(Instant.now().plus(Duration.ofSeconds(checkingInterval)));

        final var syncEventRequest = new SyncEventRequest(eventModel);
        return systemModule.getEventService().syncEventWithIdempotency(syncEventRequest)
                .map(SyncEventResponse::getCreated);
    }

    Uni<Boolean> syncVersionBuildingFailed(final Long tenantId,
                                           final Long versionId,
                                           final String idempotencyKey) {
        final var eventBody = new VersionBuildingFailedEventBodyModel(tenantId, versionId);
        final var eventModel = eventModelFactory.create(eventBody,
                idempotencyKey + "/" + eventBody.getQualifier());

        final var syncEventRequest = new SyncEventRequest(eventModel);
        return systemModule.getEventService().syncEventWithIdempotency(syncEventRequest)
                .map(SyncEventResponse::getCreated);
    }


    Uni<Boolean> syncVersionBuildingFinished(final Long tenantId,
                                             final Long versionId,
                                             final String idempotencyKey) {
        final var eventBody = new VersionBuildingFinishedEventBodyModel(tenantId, versionId);
        final var eventModel = eventModelFactory.create(eventBody,
                idempotencyKey + "/" + eventBody.getQualifier());

        final var syncEventRequest = new SyncEventRequest(eventModel);
        return systemModule.getEventService().syncEventWithIdempotency(syncEventRequest)
                .map(SyncEventResponse::getCreated);
    }

    enum JenkinsRequestResultEnum {
        IN_PROGRESS,
        FINISHED,
        FAILED
    }
}
