package com.omgservers.service.module.matchmaker.impl.service.matchmakerService.impl.method.matchmakerAssignment.getMatchmakerAssignment;

import com.omgservers.schema.module.matchmaker.GetMatchmakerAssignmentRequest;
import com.omgservers.schema.module.matchmaker.GetMatchmakerAssignmentResponse;
import com.omgservers.service.module.matchmaker.impl.operation.matchmakerAssignment.selectMatchmakerAssignment.SelectMatchmakerAssignmentOperation;
import com.omgservers.service.server.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetMatchmakerAssignmentMethodImpl implements GetMatchmakerAssignmentMethod {

    final SelectMatchmakerAssignmentOperation selectMatchmakerAssignmentOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<GetMatchmakerAssignmentResponse> getMatchmakerAssignment(final GetMatchmakerAssignmentRequest request) {
        log.debug("Get matchmaker assignment, request={}", request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shard -> {
                    final var matchmakerId = request.getMatchmakerId();
                    final var id = request.getId();
                    return pgPool.withTransaction(sqlConnection -> selectMatchmakerAssignmentOperation
                            .selectMatchmakerAssignment(sqlConnection, shard.shard(), matchmakerId, id));
                })
                .map(GetMatchmakerAssignmentResponse::new);
    }
}
