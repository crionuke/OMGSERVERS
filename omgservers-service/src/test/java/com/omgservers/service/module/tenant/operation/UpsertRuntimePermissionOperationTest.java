package com.omgservers.service.module.tenant.operation;

import com.omgservers.model.tenantPermission.TenantPermissionEnum;
import com.omgservers.service.exception.ServerSideConflictException;
import com.omgservers.service.factory.TenantModelFactory;
import com.omgservers.service.factory.TenantPermissionModelFactory;
import com.omgservers.service.module.tenant.impl.operation.upsertTenantPermission.UpsertTenantPermissionOperation;
import com.omgservers.service.operation.generateId.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class UpsertRuntimePermissionOperationTest extends Assertions {
    private static final long TIMEOUT = 1L;

    @Inject
    UpsertTenantPermissionOperation upsertTenantPermissionOperation;

    @Inject
    UpsertTenantOperationTestInterface upsertTenantOperation;

    @Inject
    TenantModelFactory tenantModelFactory;

    @Inject
    TenantPermissionModelFactory tenantPermissionModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenTenant_whenUpsertTenantPermission_thenInserted() {
        final var shard = 0;
        final var tenant = tenantModelFactory.create();
        upsertTenantOperation.upsertTenant(shard, tenant);

        final var permission =
                tenantPermissionModelFactory.create(tenant.getId(), userId(), TenantPermissionEnum.CREATE_PROJECT);
        assertTrue(upsertTenantPermissionOperation.upsertTenantPermission(TIMEOUT, pgPool, shard, permission));
    }

    @Test
    void givenPermission_whenUpsertTenantPermission_thenUpdated() {
        final var shard = 0;
        final var tenant = tenantModelFactory.create();
        upsertTenantOperation.upsertTenant(shard, tenant);
        final var permission =
                tenantPermissionModelFactory.create(tenant.getId(), userId(), TenantPermissionEnum.CREATE_PROJECT);
        upsertTenantPermissionOperation.upsertTenantPermission(TIMEOUT, pgPool, shard, permission);

        assertFalse(upsertTenantPermissionOperation.upsertTenantPermission(TIMEOUT, pgPool, shard, permission));
    }

    @Test
    void givenUnknownTenantUuid_whenUpsertTenantPermission_thenException() {
        final var shard = 0;

        final var permission =
                tenantPermissionModelFactory.create(tenantId(), userId(), TenantPermissionEnum.CREATE_PROJECT);
        final var exception = assertThrows(ServerSideConflictException.class, () -> upsertTenantPermissionOperation
                .upsertTenantPermission(TIMEOUT, pgPool, shard, permission));
    }

    Long userId() {
        return generateIdOperation.generateId();
    }

    Long tenantId() {
        return generateIdOperation.generateId();
    }
}