package com.omgservers.service.handler.impl.tenant;

import com.omgservers.schema.model.tenantFilesArchive.TenantFilesArchiveModel;
import com.omgservers.schema.module.tenant.tenantFilesArchive.GetTenantFilesArchiveRequest;
import com.omgservers.schema.module.tenant.tenantFilesArchive.GetTenantFilesArchiveResponse;
import com.omgservers.service.event.EventModel;
import com.omgservers.service.event.EventQualifierEnum;
import com.omgservers.service.event.body.module.tenant.TenantFilesArchiveCreatedEventBodyModel;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.module.tenant.TenantModule;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TenantFilesArchiveDeletedEventHandlerImpl implements EventHandler {

    final TenantModule tenantModule;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.TENANT_FILES_ARCHIVE_DELETED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.debug("Handle event, {}", event);

        final var body = (TenantFilesArchiveCreatedEventBodyModel) event.getBody();
        final var tenantId = body.getTenantId();
        final var id = body.getId();

        return getTenantFilesArchive(tenantId, id)
                .flatMap(tenantFilesArchive -> {
                    log.info("Tenant files archive was deleted, tenantFilesArchive={}/{}, versionId={}",
                            tenantId,
                            tenantFilesArchive.getId(),
                            tenantFilesArchive.getVersionId());

                    return Uni.createFrom().voidItem();
                })
                .replaceWithVoid();
    }

    Uni<TenantFilesArchiveModel> getTenantFilesArchive(final Long tenantId, final Long id) {
        final var request = new GetTenantFilesArchiveRequest(tenantId, id);
        return tenantModule.getTenantService().getTenantFilesArchive(request)
                .map(GetTenantFilesArchiveResponse::getTenantFilesArchive);
    }
}
