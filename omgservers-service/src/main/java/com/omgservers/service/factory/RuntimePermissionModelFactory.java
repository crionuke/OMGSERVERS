package com.omgservers.service.factory;

import com.omgservers.model.runtimePermission.RuntimePermissionEnum;
import com.omgservers.model.runtimePermission.RuntimePermissionModel;
import com.omgservers.service.operation.generateId.GenerateIdOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class RuntimePermissionModelFactory {

    final GenerateIdOperation generateIdOperation;

    public RuntimePermissionModel create(final Long runtimeId,
                                         final Long userId,
                                         final RuntimePermissionEnum permission) {
        final var id = generateIdOperation.generateId();
        final var idempotencyKey = generateIdOperation.generateStringId();
        return create(id, runtimeId, userId, permission, idempotencyKey);
    }

    public RuntimePermissionModel create(final Long runtimeId,
                                         final Long userId,
                                         final RuntimePermissionEnum permission,
                                         final String idempotencyKey) {
        final var id = generateIdOperation.generateId();
        return create(id, runtimeId, userId, permission, idempotencyKey);
    }

    static public RuntimePermissionModel create(final Long id,
                                                final Long runtimeId,
                                                final Long userId,
                                                final RuntimePermissionEnum permission,
                                                final String idempotencyKey) {
        final var now = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        final var permissionModel = new RuntimePermissionModel();
        permissionModel.setId(id);
        permissionModel.setIdempotencyKey(idempotencyKey);
        permissionModel.setRuntimeId(runtimeId);
        permissionModel.setCreated(now);
        permissionModel.setModified(now);
        permissionModel.setUserId(userId);
        permissionModel.setPermission(permission);
        permissionModel.setDeleted(false);
        return permissionModel;
    }

}
