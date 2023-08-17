package com.omgservers.application.module.versionModule.impl.operation.upsertVersionOperation;

import com.omgservers.application.module.versionModule.model.VersionModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;

public interface UpsertVersionOperation {
    Uni<Boolean> upsertVersion(SqlConnection sqlConnection,
                               int shard,
                               VersionModel version);

    default Boolean upsertVersion(long timeout, PgPool pgPool, int shard, VersionModel version) {
        return pgPool.withTransaction(sqlConnection -> upsertVersion(sqlConnection, shard, version))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
