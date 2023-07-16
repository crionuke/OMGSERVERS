package com.omgservers.application.module.versionModule.impl.operation.selectVersionOperation;

import com.omgservers.application.module.versionModule.model.VersionModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.UUID;

public interface SelectVersionOperation {
    Uni<VersionModel> selectVersion(SqlConnection sqlConnection,
                                    int shard,
                                    UUID uuid);

    default VersionModel selectVersion(long timeout, PgPool pgPool, int shard, UUID uuid) {
        return pgPool.withTransaction(sqlConnection -> selectVersion(sqlConnection, shard, uuid))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
