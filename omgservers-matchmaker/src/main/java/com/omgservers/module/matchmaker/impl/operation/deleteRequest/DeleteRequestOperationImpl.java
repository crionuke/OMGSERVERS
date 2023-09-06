package com.omgservers.module.matchmaker.impl.operation.deleteRequest;

import com.omgservers.model.event.body.MatchmakerDeletedEventBodyModel;
import com.omgservers.module.internal.factory.LogModelFactory;
import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.operation.executeChange.ExecuteChangeOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteRequestOperationImpl implements DeleteRequestOperation {

    final ExecuteChangeOperation executeChangeOperation;
    final LogModelFactory logModelFactory;

    @Override
    public Uni<Boolean> deleteRequest(final ChangeContext<?> changeContext,
                                      final SqlConnection sqlConnection,
                                      final int shard,
                                      final Long matchmakerId,
                                      final Long id) {
        return executeChangeOperation.executeChange(
                changeContext, sqlConnection, shard,
                """
                        delete from $schema.tab_matchmaker_request
                        where matchmaker_id = $1 and id = $2
                        """,
                Arrays.asList(matchmakerId, id),
                () -> new MatchmakerDeletedEventBodyModel(id),
                () -> logModelFactory.create(String.format("Request was deleted, " +
                        "matchmakerId=%d, id=%d", matchmakerId, id))
        );
    }
}
