package com.omgservers.application.module.tenantModule.impl.operation.deleteStageOperation;

import com.omgservers.application.operation.prepareShardSqlOperation.PrepareShardSqlOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteStageOperationImpl implements DeleteStageOperation {

    static private final String sql = """
            delete from $schema.tab_project_stage where id = $1
            """;

    final PrepareShardSqlOperation prepareShardSqlOperation;

    @Override
    public Uni<Boolean> deleteStage(final SqlConnection sqlConnection,
                                    final int shard,
                                    final Long id) {
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
                        log.info("Stage was deleted, shard={}, id={}", shard, id);
                    } else {
                        log.warn("Stage was not found, skip operation, shard={}, id={}", shard, id);
                    }
                });
    }
}
