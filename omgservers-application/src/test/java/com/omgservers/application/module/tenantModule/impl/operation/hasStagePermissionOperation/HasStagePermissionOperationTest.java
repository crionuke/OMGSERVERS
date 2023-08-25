package com.omgservers.application.module.tenantModule.impl.operation.hasStagePermissionOperation;

import com.omgservers.application.module.tenantModule.impl.operation.upsertProjectOperation.UpsertProjectOperation;
import com.omgservers.application.module.tenantModule.impl.operation.upsertStageOperation.UpsertStageOperation;
import com.omgservers.application.module.tenantModule.impl.operation.upsertStagePermissionOperation.UpsertStagePermissionOperation;
import com.omgservers.application.module.tenantModule.impl.operation.upsertTenantOperation.UpsertTenantOperation;
import com.omgservers.model.project.ProjectConfigModel;
import com.omgservers.base.factory.ProjectModelFactory;
import com.omgservers.model.stage.StageConfigModel;
import com.omgservers.base.factory.StageModelFactory;
import com.omgservers.model.stagePermission.StagePermissionEnum;
import com.omgservers.base.factory.StagePermissionModelFactory;
import com.omgservers.model.tenant.TenantConfigModel;
import com.omgservers.base.factory.TenantModelFactory;
import com.omgservers.base.impl.operation.generateIdOperation.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class HasStagePermissionOperationTest extends Assertions {
    static private final long TIMEOUT = 1L;

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
        final var project = projectModelFactory.create(tenant.getId(), userId, ProjectConfigModel.create());
        upsertProjectOperation.upsertProject(TIMEOUT, pgPool, shard, project);
        final var stage = stageModelFactory.create(project.getId(), matchmakerId(), StageConfigModel.create());
        upsertStageOperation.upsertStage(TIMEOUT, pgPool, shard, stage);
        final var permission = stagePermissionModelFactory.create(stage.getId(), userId, StagePermissionEnum.CREATE_VERSION);
        upsertStagePermissionOperation.upsertStagePermission(TIMEOUT, pgPool, shard, permission);

        assertTrue(hasStagePermissionOperation.hasStagePermission(TIMEOUT, pgPool, shard, stage.getId(), permission.getUserId(), permission.getPermission()));
    }

    @Test
    void givenUnknownUuids_whenHasStagePermission_thenNo() {
        final var shard = 0;

        assertFalse(hasStagePermissionOperation.hasStagePermission(TIMEOUT, pgPool, shard, projectId(), userId(), StagePermissionEnum.CREATE_VERSION));
    }

    Long userId() {
        return generateIdOperation.generateId();
    }

    Long projectId() {
        return generateIdOperation.generateId();
    }

    Long versionId() {
        return generateIdOperation.generateId();
    }

    Long matchmakerId() {
        return generateIdOperation.generateId();
    }
}