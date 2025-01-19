package com.omgservers.service.module.matchmaker.impl.operation.matchmakerAssignment;

import com.omgservers.schema.model.matchmakerAssignment.MatchmakerAssignmentModel;
import com.omgservers.service.module.matchmaker.impl.mappers.MatchmakerAssignmentModelMapper;
import com.omgservers.service.operation.server.SelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectMatchmakerAssignmentOperationImpl implements SelectMatchmakerAssignmentOperation {

    final SelectObjectOperation selectObjectOperation;

    final MatchmakerAssignmentModelMapper matchmakerAssignmentModelMapper;

    @Override
    public Uni<MatchmakerAssignmentModel> execute(final SqlConnection sqlConnection,
                                                  final int shard,
                                                  final Long matchmakerId,
                                                  final Long id) {
        return selectObjectOperation.selectObject(
                sqlConnection,
                shard,
                """
                        select
                            id, idempotency_key, matchmaker_id, created, modified, client_id, deleted
                        from $schema.tab_matchmaker_assignment
                        where matchmaker_id = $1 and id = $2
                        limit 1
                        """,
                List.of(
                        matchmakerId,
                        id
                ),
                "Matchmaker assignment",
                matchmakerAssignmentModelMapper::execute);
    }
}
