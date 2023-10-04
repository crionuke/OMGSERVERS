package com.omgservers.operation.executeSelectList;

import com.omgservers.module.system.factory.EventModelFactory;
import com.omgservers.module.system.factory.LogModelFactory;
import com.omgservers.module.system.impl.operation.upsertEvent.UpsertEventOperation;
import com.omgservers.module.system.impl.operation.upsertLog.UpsertLogOperation;
import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class ExecuteSelectListOperationImpl implements ExecuteSelectListOperation {

    final TransformPgExceptionOperation transformPgExceptionOperation;
    final PrepareShardSqlOperation prepareShardSqlOperation;
    final UpsertEventOperation upsertEventOperation;
    final UpsertLogOperation upsertLogOperation;

    final EventModelFactory eventModelFactory;
    final LogModelFactory logModelFactory;

    @Override
    public <T> Uni<List<T>> executeSelectList(final SqlConnection sqlConnection,
                                              final int shard,
                                              final String sql,
                                              final List<?> parameters,
                                              final String objectName,
                                              final Function<Row, T> objectMapper) {
        var preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);
        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.from(parameters))
                .map(RowSet::iterator)
                .map(iterator -> {
                    final List<T> objects = new ArrayList<T>();
                    while (iterator.hasNext()) {
                        try {
                            final var object = objectMapper.apply(iterator.next());
                            objects.add(object);
                        } catch (Exception e) {
                            log.error("Skip {}, {}", objectName.toLowerCase(), e.getMessage());
                        }
                    }
                    if (objects.size() > 0) {
                        log.debug("List of {}/s were selected, parameters={}",
                                objectName.toLowerCase(), parameters);
                    } else {
                        log.debug("{}/s were not found, parameters={}", objectName, parameters);
                    }
                    return objects;
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException(preparedSql, (PgException) t));
    }
}
