package com.omgservers.module.matchmaker.impl.operation.deleteMatchmaker;

import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteMatchmakerOperationImpl implements DeleteMatchmakerOperation {

    static private final String sql = """
            delete from $schema.tab_matchmaker where id = $1
            """;

    final TransformPgExceptionOperation transformPgExceptionOperation;
    final PrepareShardSqlOperation prepareShardSqlOperation;

    @Override
    public Uni<Boolean> deleteMatchmaker(SqlConnection sqlConnection, int shard, Long id) {
        if (sqlConnection == null) {
            throw new IllegalArgumentException("sqlConnection is null");
        }
        if (id == null) {
            throw new IllegalArgumentException("uuid is null");
        }

        String preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);
        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.of(id))
                .map(rowSet -> rowSet.rowCount() > 0)
                .invoke(deleted -> {
                    if (deleted) {
                        log.info("Matchmaker was deleted, shard={}, id={}", shard, id);
                    } else {
                        log.warn("Matchmaker was not found, skip operation, shard={}, id={}", shard, id);
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException((PgException) t));
    }
}
