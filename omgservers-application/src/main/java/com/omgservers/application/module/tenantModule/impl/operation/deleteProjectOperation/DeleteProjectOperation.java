package com.omgservers.application.module.tenantModule.impl.operation.deleteProjectOperation;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.UUID;

public interface DeleteProjectOperation {
    Uni<Boolean> deleteProject(SqlConnection sqlConnection, int shard, UUID uuid);

    default Boolean deleteProject(long timeout, PgPool pgPool, int shard, UUID uuid) {
        return pgPool.withTransaction(sqlConnection -> deleteProject(sqlConnection, shard, uuid))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
