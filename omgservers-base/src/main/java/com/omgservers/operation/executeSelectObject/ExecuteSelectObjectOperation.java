package com.omgservers.operation.executeSelectObject;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.util.List;
import java.util.function.Function;

public interface ExecuteSelectObjectOperation {

    <T> Uni<T> executeSelectObject(SqlConnection sqlConnection,
                                   int shard,
                                   String sql,
                                   List<?> parameters,
                                   String objectName,
                                   Function<Row, T> objectMapper);
}
