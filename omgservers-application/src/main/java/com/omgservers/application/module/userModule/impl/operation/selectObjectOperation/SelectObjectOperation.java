package com.omgservers.application.module.userModule.impl.operation.selectObjectOperation;

import com.omgservers.application.module.userModule.model.object.ObjectModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.UUID;

public interface SelectObjectOperation {
    Uni<ObjectModel> selectObject(SqlConnection sqlConnection,
                                  int shard,
                                  UUID playerUuid,
                                  String name);

    default ObjectModel selectObject(long timeout, PgPool pgPool, int shard, UUID playerUuid, String name) {
        return pgPool.withTransaction(sqlConnection -> selectObject(sqlConnection, shard, playerUuid, name))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
