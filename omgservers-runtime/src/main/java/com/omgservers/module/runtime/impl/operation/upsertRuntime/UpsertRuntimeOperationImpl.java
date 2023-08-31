package com.omgservers.module.runtime.impl.operation.upsertRuntime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.exception.ServerSideConflictException;
import com.omgservers.exception.ServerSideInternalException;
import com.omgservers.model.runtime.RuntimeModel;
import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertRuntimeOperationImpl implements UpsertRuntimeOperation {

    static private final String sql = """
            insert into $schema.tab_runtime(id, created, modified, tenant_id, stage_id, version_id, matchmaker_id, match_id, type, current_step, config)
            values($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
            on conflict (id) do
            nothing
            """;

    final PrepareShardSqlOperation prepareShardSqlOperation;
    final ObjectMapper objectMapper;

    @Override
    public Uni<Boolean> upsertRuntime(final SqlConnection sqlConnection,
                                      final int shard,
                                      final RuntimeModel runtime) {
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }
        if (runtime == null) {
            throw new ServerSideBadRequestException("runtime is null");
        }

        return upsertQuery(sqlConnection, shard, runtime)
                .invoke(inserted -> {
                    if (inserted) {
                        log.info("Runtime was inserted, shard={}, runtime={}", shard, runtime);
                    } else {
                        log.info("Runtime was updated, shard={}, runtime={}", shard, runtime);
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> new ServerSideConflictException(String
                        .format("unhandled PgException, %s, runtime=%s", t.getMessage(), runtime)));
    }

    Uni<Boolean> upsertQuery(final SqlConnection sqlConnection,
                             final int shard,
                             final RuntimeModel runtime) {
        try {
            var preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);
            var configString = objectMapper.writeValueAsString(runtime.getConfig());
            return sqlConnection.preparedQuery(preparedSql)
                    .execute(Tuple.from(new ArrayList<>() {{
                        add(runtime.getId());
                        add(runtime.getCreated().atOffset(ZoneOffset.UTC));
                        add(runtime.getModified().atOffset(ZoneOffset.UTC));
                        add(runtime.getTenantId());
                        add(runtime.getStageId());
                        add(runtime.getVersionId());
                        add(runtime.getMatchmakerId());
                        add(runtime.getMatchId());
                        add(runtime.getType());
                        add(runtime.getCurrentStep());
                        add(configString);
                    }}))
                    .map(rowSet -> rowSet.rowCount() > 0);
        } catch (IOException e) {
            throw new ServerSideInternalException(e.getMessage(), e);
        }
    }
}
