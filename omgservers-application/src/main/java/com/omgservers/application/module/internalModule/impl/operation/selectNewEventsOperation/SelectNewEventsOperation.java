package com.omgservers.application.module.internalModule.impl.operation.selectNewEventsOperation;

import com.omgservers.application.module.internalModule.model.event.EventModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.List;

public interface SelectNewEventsOperation {
    Uni<List<EventModel>> selectNewEvents(SqlConnection sqlConnection, int limit);

    default List<EventModel> selectNewEvents(long timeout, PgPool pgPool, int limit) {
        return pgPool.withTransaction(sqlConnection -> selectNewEvents(sqlConnection, limit))
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
