package com.omgservers.service.module.matchmaker.impl.operation.matchmakerCommand;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.schema.model.exception.ExceptionQualifierEnum;
import com.omgservers.schema.model.matchmakerCommand.MatchmakerCommandModel;
import com.omgservers.service.exception.ServerSideBadRequestException;
import com.omgservers.service.factory.lobby.LogModelFactory;
import com.omgservers.service.operation.server.ChangeContext;
import com.omgservers.service.operation.server.ChangeObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertMatchmakerCommandOperationImpl implements UpsertMatchmakerCommandOperation {

    final ChangeObjectOperation changeObjectOperation;

    final LogModelFactory logModelFactory;
    final ObjectMapper objectMapper;

    @Override
    public Uni<Boolean> execute(final ChangeContext<?> changeContext,
                                final SqlConnection sqlConnection,
                                final int shard,
                                final MatchmakerCommandModel matchmakerCommand) {
        return changeObjectOperation.changeObject(
                changeContext, sqlConnection, shard,
                """
                        insert into $schema.tab_matchmaker_command(
                            id, idempotency_key, matchmaker_id, created, modified, qualifier, body, deleted)
                        values($1, $2, $3, $4, $5, $6, $7, $8)
                        on conflict (id) do
                        nothing
                        """,
                List.of(
                        matchmakerCommand.getId(),
                        matchmakerCommand.getIdempotencyKey(),
                        matchmakerCommand.getMatchmakerId(),
                        matchmakerCommand.getCreated().atOffset(ZoneOffset.UTC),
                        matchmakerCommand.getModified().atOffset(ZoneOffset.UTC),
                        matchmakerCommand.getQualifier(),
                        getBodyString(matchmakerCommand),
                        matchmakerCommand.getDeleted()
                ),
                () -> null,
                () -> null
        );
    }

    String getBodyString(final MatchmakerCommandModel matchmakerCommand) {
        try {
            return objectMapper.writeValueAsString(matchmakerCommand.getBody());
        } catch (IOException e) {
            throw new ServerSideBadRequestException(ExceptionQualifierEnum.WRONG_OBJECT, e.getMessage(), e);
        }
    }
}
