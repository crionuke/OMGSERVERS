package com.omgservers.application.module.runtimeModule.impl.operation.upsertRuntimeOperation;

import com.omgservers.application.module.runtimeModule.model.RuntimeModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;

public interface UpsertRuntimeOperation {
    Uni<Boolean> upsertRuntime(SqlConnection sqlConnection, int shard, RuntimeModel runtime);

    default Boolean upsertRuntime(long timeout, PgPool pgPool, int shard, RuntimeModel runtime) {
        return pgPool.withTransaction(sqlConnection -> upsertRuntime(sqlConnection, shard, runtime))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
