package com.omgservers.module.tenant.impl.operation.upsertProjectPermission;

import com.omgservers.exception.ServerSideNotFoundException;
import com.omgservers.model.project.ProjectConfigModel;
import com.omgservers.model.projectPermission.ProjectPermissionEnum;
import com.omgservers.model.tenant.TenantConfigModel;
import com.omgservers.module.tenant.factory.ProjectModelFactory;
import com.omgservers.module.tenant.factory.ProjectPermissionModelFactory;
import com.omgservers.module.tenant.factory.TenantModelFactory;
import com.omgservers.module.tenant.impl.operation.upsertProject.UpsertProjectOperation;
import com.omgservers.module.tenant.impl.operation.upsertTenant.UpsertTenantOperation;
import com.omgservers.operation.generateId.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class UpsertProjectPermissionOperationTest extends Assertions {
    private static final long TIMEOUT = 1L;

    @Inject
    UpsertProjectPermissionOperation upsertProjectPermissionOperation;

    @Inject
    UpsertProjectOperation upsertProjectOperation;

    @Inject
    UpsertTenantOperation upsertTenantOperation;

    @Inject
    TenantModelFactory tenantModelFactory;

    @Inject
    ProjectModelFactory projectModelFactory;

    @Inject
    ProjectPermissionModelFactory projectPermissionModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenProject_whenUpsertProjectPermission_thenInserted() {
        final var shard = 0;
        final var tenant = tenantModelFactory.create(TenantConfigModel.create());
        upsertTenantOperation.upsertTenant(TIMEOUT, pgPool, shard, tenant);
        final var project = projectModelFactory.create(tenant.getId(), ProjectConfigModel.create());
        upsertProjectOperation.upsertProject(TIMEOUT, pgPool, shard, project);

        final var permission = projectPermissionModelFactory.create(project.getId(), userId(), ProjectPermissionEnum.CREATE_STAGE);
        assertTrue(upsertProjectPermissionOperation.upsertProjectPermission(TIMEOUT, pgPool, shard, tenant.getId(), permission));
    }

    @Test
    void givenPermission_whenUpsertProjectPermission_thenUpdated() {
        final var shard = 0;
        final var tenant = tenantModelFactory.create(TenantConfigModel.create());
        upsertTenantOperation.upsertTenant(TIMEOUT, pgPool, shard, tenant);
        final var project = projectModelFactory.create(tenant.getId(), ProjectConfigModel.create());
        upsertProjectOperation.upsertProject(TIMEOUT, pgPool, shard, project);
        final var permission = projectPermissionModelFactory.create(project.getId(), userId(), ProjectPermissionEnum.CREATE_STAGE);
        upsertProjectPermissionOperation.upsertProjectPermission(TIMEOUT, pgPool, shard, tenant.getId(), permission);

        assertFalse(upsertProjectPermissionOperation.upsertProjectPermission(TIMEOUT, pgPool, shard, tenant.getId(), permission));
    }

    @Test
    void givenUnknownIds_whenUpsertProjectPermission_thenServerSideNotFoundException() {
        final var shard = 0;

        final var permission = projectPermissionModelFactory.create(projectId(), userId(), ProjectPermissionEnum.CREATE_STAGE);
        final var exception = assertThrows(ServerSideNotFoundException.class, () -> upsertProjectPermissionOperation
                .upsertProjectPermission(TIMEOUT, pgPool, shard, tenantId(), permission));
        log.info("Exception: {}", exception.getMessage());
    }

    Long userId() {
        return generateIdOperation.generateId();
    }

    Long tenantId() {
        return generateIdOperation.generateId();
    }

    Long projectId() {
        return generateIdOperation.generateId();
    }
}