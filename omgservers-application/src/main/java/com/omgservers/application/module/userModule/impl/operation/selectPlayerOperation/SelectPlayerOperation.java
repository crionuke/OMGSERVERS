package com.omgservers.application.module.userModule.impl.operation.selectPlayerOperation;

import com.omgservers.application.module.userModule.model.player.PlayerModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.UUID;

public interface SelectPlayerOperation {
    Uni<PlayerModel> selectPlayer(SqlConnection sqlConnection,
                                  int shard,
                                  UUID userUuid,
                                  UUID stageUuid);

    default PlayerModel selectPlayer(long timeout, PgPool pgPool, int shard, UUID userUuid, UUID stageUuid) {
        return pgPool.withTransaction(sqlConnection -> selectPlayer(sqlConnection, shard, userUuid, stageUuid))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
