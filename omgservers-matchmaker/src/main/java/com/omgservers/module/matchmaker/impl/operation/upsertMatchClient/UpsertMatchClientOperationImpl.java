package com.omgservers.module.matchmaker.impl.operation.upsertMatchClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.ChangeContext;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.model.event.body.MatchClientCreatedEventBodyModel;
import com.omgservers.model.matchClient.MatchClientModel;
import com.omgservers.module.internal.factory.EventModelFactory;
import com.omgservers.module.internal.factory.LogModelFactory;
import com.omgservers.module.internal.impl.operation.upsertEvent.UpsertEventOperation;
import com.omgservers.module.internal.impl.operation.upsertLog.UpsertLogOperation;
import com.omgservers.operation.prepareShardSql.PrepareShardSqlOperation;
import com.omgservers.operation.transformPgException.TransformPgExceptionOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.pgclient.PgException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneOffset;
import java.util.ArrayList;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertMatchClientOperationImpl implements UpsertMatchClientOperation {

    static private final String sql = """
            insert into $schema.tab_matchmaker_match_client(id, matchmaker_id, match_id, created, modified, user_id, client_id, request_id)
            values($1, $2, $3, $4, $5, $6, $7, $8)
            on conflict (id) do
            nothing
            """;

    final TransformPgExceptionOperation transformPgExceptionOperation;
    final PrepareShardSqlOperation prepareShardSqlOperation;
    final UpsertEventOperation upsertEventOperation;
    final UpsertLogOperation upsertLogOperation;

    final EventModelFactory eventModelFactory;
    final LogModelFactory logModelFactory;
    final ObjectMapper objectMapper;

    @Override
    public Uni<Boolean> upsertMatchClient(final ChangeContext changeContext,
                                          final SqlConnection sqlConnection,
                                          final int shard,
                                          final MatchClientModel matchClient) {
        if (changeContext == null) {
            throw new ServerSideBadRequestException("changeContext is null");
        }
        if (sqlConnection == null) {
            throw new ServerSideBadRequestException("sqlConnection is null");
        }
        if (matchClient == null) {
            throw new ServerSideBadRequestException("matchClient is null");
        }

        return upsertObject(sqlConnection, shard, matchClient)
                .call(objectWasInserted -> upsertEvent(objectWasInserted, changeContext, sqlConnection, matchClient))
                .call(objectWasInserted -> upsertLog(objectWasInserted, changeContext, sqlConnection, matchClient))
                .invoke(inserted -> {
                    if (inserted) {
                        log.info("Match client was inserted, shard={}, matchClient={}", shard, matchClient);
                    }
                })
                .onFailure(PgException.class)
                .transform(t -> transformPgExceptionOperation.transformPgException((PgException) t));
    }

    Uni<Boolean> upsertObject(SqlConnection sqlConnection, int shard, MatchClientModel matchClient) {
        var preparedSql = prepareShardSqlOperation.prepareShardSql(sql, shard);
        return sqlConnection.preparedQuery(preparedSql)
                .execute(Tuple.from(new ArrayList<>() {{
                    add(matchClient.getId());
                    add(matchClient.getMatchmakerId());
                    add(matchClient.getMatchId());
                    add(matchClient.getCreated().atOffset(ZoneOffset.UTC));
                    add(matchClient.getModified().atOffset(ZoneOffset.UTC));
                    add(matchClient.getUserId());
                    add(matchClient.getClientId());
                    add(matchClient.getRequestId());
                }}))
                .map(rowSet -> rowSet.rowCount() > 0);
    }

    Uni<Boolean> upsertEvent(final boolean objectWasInserted,
                             final ChangeContext changeContext,
                             final SqlConnection sqlConnection,
                             final MatchClientModel matchClient) {
        if (objectWasInserted) {
            final var body = new MatchClientCreatedEventBodyModel(matchClient.getMatchmakerId(), matchClient.getId());
            final var event = eventModelFactory.create(body);
            return upsertEventOperation.upsertEvent(sqlConnection, event)
                    .invoke(eventWasInserted -> {
                        if (eventWasInserted) {
                            changeContext.add(event);
                        }
                    });
        } else {
            return Uni.createFrom().item(false);
        }
    }

    Uni<Boolean> upsertLog(final boolean objectWasInserted,
                           final ChangeContext changeContext,
                           final SqlConnection sqlConnection,
                           final MatchClientModel matchClient) {
        if (objectWasInserted) {
            final var changeLog = logModelFactory.create("Match client was inserted, matchClient=" + matchClient);
            return upsertLogOperation.upsertLog(sqlConnection, changeLog)
                    .invoke(logWasInserted -> {
                        if (logWasInserted) {
                            changeContext.add(changeLog);
                        }
                    });
        } else {
            return Uni.createFrom().item(false);
        }
    }
}
