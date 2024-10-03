package com.omgservers.service.entrypoint.developer.impl.service.developerService.impl.operation;

import com.omgservers.schema.model.tenantProjectPermission.TenantProjectPermissionModel;
import com.omgservers.schema.model.tenantProjectPermission.TenantProjectPermissionQualifierEnum;
import com.omgservers.schema.module.tenant.tenantProjectPermission.SyncTenantProjectPermissionRequest;
import com.omgservers.service.factory.tenant.TenantProjectPermissionModelFactory;
import com.omgservers.service.module.tenant.TenantModule;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class CreateTenantProjectPermissionOperationImpl implements CreateTenantProjectPermissionOperation {

    final TenantModule tenantModule;

    final TenantProjectPermissionModelFactory tenantProjectPermissionModelFactory;

    @Override
    public Uni<TenantProjectPermissionModel> execute(final Long tenantId,
                                                     final Long tenantProjectId,
                                                     final Long userId,
                                                     final TenantProjectPermissionQualifierEnum permissionQualifier) {
        final var projectPermission = tenantProjectPermissionModelFactory.create(tenantId,
                tenantProjectId,
                userId,
                permissionQualifier);
        final var request = new SyncTenantProjectPermissionRequest(projectPermission);
        return tenantModule.getTenantService().syncTenantProjectPermission(request)
                .replaceWith(projectPermission);
    }
}
