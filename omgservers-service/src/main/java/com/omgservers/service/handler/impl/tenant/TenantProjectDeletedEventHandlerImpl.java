package com.omgservers.service.handler.impl.tenant;

import com.omgservers.schema.model.project.TenantProjectModel;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectRequest;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectResponse;
import com.omgservers.service.event.EventModel;
import com.omgservers.service.event.EventQualifierEnum;
import com.omgservers.service.event.body.module.tenant.TenantProjectDeletedEventBodyModel;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.shard.tenant.TenantShard;
import com.omgservers.service.operation.alias.DeleteAliasesByEntityIdOperation;
import com.omgservers.service.operation.tenant.DeleteTenantProjectPermissionsOperation;
import com.omgservers.service.operation.tenant.DeleteTenantStagesByTenantProjectIdOperation;
import com.omgservers.service.operation.tenant.DeleteTenantVersionsByTenantProjectIdOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class TenantProjectDeletedEventHandlerImpl implements EventHandler {

    final TenantShard tenantShard;

    final DeleteTenantVersionsByTenantProjectIdOperation deleteTenantVersionsByTenantProjectIdOperation;
    final DeleteTenantStagesByTenantProjectIdOperation deleteTenantStagesByTenantProjectIdOperation;
    final DeleteTenantProjectPermissionsOperation deleteTenantProjectPermissionsOperation;
    final DeleteAliasesByEntityIdOperation deleteAliasesByEntityIdOperation;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.TENANT_PROJECT_DELETED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.trace("Handle event, {}", event);

        final var body = (TenantProjectDeletedEventBodyModel) event.getBody();
        final var tenantId = body.getTenantId();
        final var tenantProjectId = body.getId();

        return getTenantProject(tenantId, tenantProjectId)
                .flatMap(tenantProject -> {
                    log.debug("Deleted, {}", tenantProject);

                    return deleteTenantProjectPermissionsOperation.execute(tenantId, tenantProjectId)
                            .flatMap(voidItem -> deleteTenantStagesByTenantProjectIdOperation
                                    .execute(tenantId, tenantProjectId))
                            .flatMap(voidItem -> deleteTenantVersionsByTenantProjectIdOperation
                                    .execute(tenantId, tenantProjectId))
                            .flatMap(voidItem -> deleteAliasesByEntityIdOperation
                                    .execute(tenantId, tenantProjectId));
                })
                .replaceWithVoid();
    }

    Uni<TenantProjectModel> getTenantProject(final Long tenantId, final Long id) {
        final var request = new GetTenantProjectRequest(tenantId, id);
        return tenantShard.getService().getTenantProject(request)
                .map(GetTenantProjectResponse::getTenantProject);
    }

}
