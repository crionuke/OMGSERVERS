package com.omgservers.module.tenant.operation;

import com.omgservers.model.project.ProjectConfigModel;
import com.omgservers.model.stage.StageConfigModel;
import com.omgservers.model.stagePermission.StagePermissionEnum;
import com.omgservers.model.tenant.TenantConfigModel;
import com.omgservers.factory.ProjectModelFactory;
import com.omgservers.factory.StageModelFactory;
import com.omgservers.factory.StagePermissionModelFactory;
import com.omgservers.factory.TenantModelFactory;
import com.omgservers.module.tenant.impl.operation.hasStagePermission.HasStagePermissionOperation;
import com.omgservers.module.tenant.impl.operation.upsertProject.UpsertProjectOperation;
import com.omgservers.module.tenant.impl.operation.upsertStage.UpsertStageOperation;
import com.omgservers.module.tenant.impl.operation.upsertStagePermission.UpsertStagePermissionOperation;
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
class HasStagePermissionOperationTest extends Assertions {
    private static final long TIMEOUT = 1L;

    @Inject
    HasStagePermissionOperation hasStagePermissionOperation;

    @Inject
    UpsertStagePermissionOperation upsertStagePermissionOperation;

    @Inject
    UpsertTenantOperation upsertTenantOperation;

    @Inject
    UpsertProjectOperation upsertProjectOperation;

    @Inject
    UpsertStageOperation upsertStageOperation;

    @Inject
    TenantModelFactory tenantModelFactory;

    @Inject
    ProjectModelFactory projectModelFactory;

    @Inject
    StageModelFactory stageModelFactory;

    @Inject
    StagePermissionModelFactory stagePermissionModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenStagePermission_whenHasStagePermission_thenYes() {
        final var shard = 0;
        final var userId = userId();
        final var tenant = tenantModelFactory.create(TenantConfigModel.create());
        upsertTenantOperation.upsertTenant(TIMEOUT, pgPool, shard, tenant);
        final var project = projectModelFactory.create(tenant.getId(), ProjectConfigModel.create());
        upsertProjectOperation.upsertProject(TIMEOUT, pgPool, shard, project);
        final var stage = stageModelFactory.create(tenant.getId(), project.getId(), StageConfigModel.create());
        upsertStageOperation.upsertStage(TIMEOUT, pgPool, shard, tenant.getId(), stage);
        final var permission = stagePermissionModelFactory.create(tenant.getId(), stage.getId(), userId, StagePermissionEnum.CREATE_VERSION);
        upsertStagePermissionOperation.upsertStagePermission(TIMEOUT, pgPool, shard, permission);

        assertTrue(hasStagePermissionOperation.hasStagePermission(TIMEOUT, pgPool, shard, tenant.getId(), stage.getId(), permission.getUserId(), permission.getPermission()));
    }

    @Test
    void givenUnknownIds_whenHasStagePermission_thenFalse() {
        final var shard = 0;

        assertFalse(hasStagePermissionOperation.hasStagePermission(TIMEOUT, pgPool, shard, tenantId(), projectId(), userId(), StagePermissionEnum.CREATE_VERSION));
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