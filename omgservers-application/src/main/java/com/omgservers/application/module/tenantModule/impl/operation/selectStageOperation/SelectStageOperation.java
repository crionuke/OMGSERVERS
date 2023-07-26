package com.omgservers.application.module.tenantModule.impl.operation.selectStageOperation;

import com.omgservers.application.module.tenantModule.model.stage.StageModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.UUID;

public interface SelectStageOperation {
    Uni<StageModel> selectStage(SqlConnection sqlConnection,
                                int shard,
                                Long id);

    default StageModel selectStage(long timeout, PgPool pgPool, int shard, Long id) {
        return pgPool.withTransaction(sqlConnection -> selectStage(sqlConnection, shard, id))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
