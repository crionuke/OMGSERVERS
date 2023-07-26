package com.omgservers.application.module.tenantModule.impl.operation.hasStagePermissionOperation;

import com.omgservers.application.module.tenantModule.model.stage.StagePermissionEnum;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.UUID;

public interface HasStagePermissionOperation {
    Uni<Boolean> hasStagePermission(SqlConnection sqlConnection,
                                    int shard,
                                    Long stageId,
                                    Long userId,
                                    StagePermissionEnum permission);

    default Boolean hasStagePermission(long timeout,
                                       PgPool pgPool,
                                       int shard,
                                       Long stageId,
                                       Long userId,
                                       StagePermissionEnum permission) {
        return pgPool.withTransaction(sqlConnection ->
                        hasStagePermission(sqlConnection, shard, stageId, userId, permission))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
