package com.omgservers.application.module.matchmakerModule.impl.operation.selectRequestOperation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.application.exception.ServerSideConflictException;
import com.omgservers.application.exception.ServerSideNotFoundException;
import com.omgservers.application.module.matchmakerModule.model.request.RequestConfigModel;
import com.omgservers.application.module.matchmakerModule.model.request.RequestModel;
import com.omgservers.application.operation.prepareShardSqlOperation.PrepareShardSqlOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectRequestOperationImpl implements SelectRequestOperation {

    static private final String sql = """
            select id, matchmaker_id, created, config
            from $schema.tab_matchmaker_request
            where id = $1
            limit 1
            """;

    final PrepareShardSqlOperation prepareShardSqlOperation;
    final ObjectMapper objectMapper;

    @Override
    public Uni<RequestModel> selectRequest(final SqlConnection sqlConnection,
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
                .map(RowSet::iterator)
                .map(iterator -> {
                    if (iterator.hasNext()) {
                        try {
                            final var matchmakerRequest = createMatchmakerRequest(iterator.next());
                            log.info("Matchmaker request was found, matchmakerRequest={}", matchmakerRequest);
                            return matchmakerRequest;
                        } catch (IOException e) {
                            throw new ServerSideConflictException("matchmaker request can't be parsed, id=" + id, e);
                        }
                    } else {
                        throw new ServerSideNotFoundException("matchmaker request was not found, id=" + id);
                    }
                });
    }

    RequestModel createMatchmakerRequest(Row row) throws IOException {
        RequestModel request = new RequestModel();
        request.setId(row.getLong("id"));
        request.setMatchmakerId(row.getLong("matchmaker_id"));
        request.setCreated(row.getOffsetDateTime("created").toInstant());
        request.setConfig(objectMapper.readValue(row.getString("config"), RequestConfigModel.class));
        return request;
    }
}
