package com.omgservers.module.matchmaker.impl.service.matchmakerService.impl.method.viewRequests;

import com.omgservers.dto.matchmaker.ViewRequestsRequest;
import com.omgservers.dto.matchmaker.ViewRequestsResponse;
import com.omgservers.module.matchmaker.impl.operation.selectRequestsByMatchmakerId.SelectRequestsByMatchmakerIdOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class ViewRequestsMethodImpl implements ViewRequestsMethod {

    final SelectRequestsByMatchmakerIdOperation selectRequestsByMatchmakerIdOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<ViewRequestsResponse> viewRequests(ViewRequestsRequest request) {
        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shard -> {
                    final var matchmakerId = request.getMatchmakerId();
                    return pgPool.withTransaction(sqlConnection -> selectRequestsByMatchmakerIdOperation
                            .selectRequestsByMatchmakerId(sqlConnection, shard.shard(), matchmakerId));
                })
                .map(ViewRequestsResponse::new);

    }
}
