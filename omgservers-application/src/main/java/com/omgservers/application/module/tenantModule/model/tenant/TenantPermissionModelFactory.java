package com.omgservers.application.module.tenantModule.model.tenant;

import com.omgservers.application.operation.generateIdOperation.GenerateIdOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class TenantPermissionModelFactory {

    final GenerateIdOperation generateIdOperation;

    public TenantPermissionModel create(
            final Long tenantId,
            final Long userId,
            final TenantPermissionEnum permission) {
        final var id = generateIdOperation.generateId();
        return create(id, tenantId, userId, permission);
    }

    static public TenantPermissionModel create(final Long id,
                                               final Long tenantId,
                                               final Long userId,
                                               final TenantPermissionEnum permission) {
        Instant now = Instant.now();

        TenantPermissionModel permissionModel = new TenantPermissionModel();
        permissionModel.setId(id);
        permissionModel.setTenantId(tenantId);
        permissionModel.setCreated(now);
        permissionModel.setUserId(userId);
        permissionModel.setPermission(permission);
        return permissionModel;
    }

}
