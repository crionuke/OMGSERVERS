package com.omgservers.application.module.tenantModule.impl.operation.upsertTenantOperation;

import com.omgservers.application.module.tenantModule.model.tenant.TenantConfigModel;
import com.omgservers.application.module.tenantModule.model.tenant.TenantModel;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@Slf4j
@QuarkusTest
class UpsertTenantOperationTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    UpsertTenantOperation upsertTenantOperation;

    @Inject
    PgPool pgPool;

    @Test
    void whenUpsertTenant_thenInserted() {
        final var shard = 0;
        final var tenant = TenantModel.create(TenantConfigModel.create());
        assertTrue(upsertTenantOperation.upsertTenant(TIMEOUT, pgPool, shard, tenant));
    }

    @Test
    void givenTenant_whenUpsertTenant_thenUpdated() {
        final var shard = 0;
        final var tenant = TenantModel.create(TenantConfigModel.create());
        upsertTenantOperation.upsertTenant(TIMEOUT, pgPool, shard, tenant);

        assertFalse(upsertTenantOperation.upsertTenant(TIMEOUT, pgPool, shard, tenant));
    }
}