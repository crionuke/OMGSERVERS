package com.omgservers.module.matchmaker.impl.operation.upsertMatchCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.model.matchCommand.MatchCommandModel;
import com.omgservers.factory.LogModelFactory;
import com.omgservers.operation.changeObject.ChangeObjectOperation;
import com.omgservers.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertMatchCommandOperationImpl implements UpsertMatchCommandOperation {

    final ChangeObjectOperation changeObjectOperation;

    final LogModelFactory logModelFactory;
    final ObjectMapper objectMapper;

    @Override
    public Uni<Boolean> upsertMatchCommand(final ChangeContext<?> changeContext,
                                           final SqlConnection sqlConnection,
                                           final int shard,
                                           final MatchCommandModel matchCommand) {
        return changeObjectOperation.changeObject(
                changeContext, sqlConnection, shard,
                """
                        insert into $schema.tab_matchmaker_match_command(
                            id, matchmaker_id, match_id, created, modified, qualifier, body)
                        values($1, $2, $3, $4, $5, $6, $7)
                        on conflict (id) do
                        nothing
                        """,
                Arrays.asList(
                        matchCommand.getId(),
                        matchCommand.getMatchmakerId(),
                        matchCommand.getMatchId(),
                        matchCommand.getCreated().atOffset(ZoneOffset.UTC),
                        matchCommand.getModified().atOffset(ZoneOffset.UTC),
                        matchCommand.getQualifier(),
                        getBodyString(matchCommand)
                ),
                () -> null,
                () -> logModelFactory.create("Match command was inserted, matchCommand=" + matchCommand)
        );
    }

    String getBodyString(MatchCommandModel matchCommand) {
        try {
            return objectMapper.writeValueAsString(matchCommand.getBody());
        } catch (IOException e) {
            throw new ServerSideBadRequestException(e.getMessage(), e);
        }
    }
}
