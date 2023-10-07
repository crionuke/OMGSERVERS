package com.omgservers.module.matchmaker.impl.service.matchmakerService.impl.method.findMatchClient;

import com.omgservers.dto.matchmaker.FindMatchClientRequest;
import com.omgservers.dto.matchmaker.FindMatchClientResponse;
import com.omgservers.module.matchmaker.impl.operation.selectMatchClientByMatchmakerIdAndClientId.SelectMatchClientByMatchmakerIdAndClientIdOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class FindMatchClientMethodImpl implements FindMatchClientMethod {

    final SelectMatchClientByMatchmakerIdAndClientIdOperation selectMatchClientByMatchmakerIdAndClientIdOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<FindMatchClientResponse> findMatchClient(final FindMatchClientRequest request) {
        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shard -> {
                    final var matchmakerId = request.getMatchmakerId();
                    final var clientId = request.getClientId();
                    return pgPool.withTransaction(sqlConnection -> selectMatchClientByMatchmakerIdAndClientIdOperation
                            .selectMatchClientByMatchmakerIdAndClientId(sqlConnection,
                                    shard.shard(),
                                    matchmakerId,
                                    clientId));
                })
                .map(FindMatchClientResponse::new);
    }
}
