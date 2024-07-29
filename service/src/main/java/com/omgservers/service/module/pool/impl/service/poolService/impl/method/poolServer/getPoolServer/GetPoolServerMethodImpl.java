package com.omgservers.service.module.pool.impl.service.poolService.impl.method.poolServer.getPoolServer;

import com.omgservers.schema.module.pool.poolServer.GetPoolServerRequest;
import com.omgservers.schema.module.pool.poolServer.GetPoolServerResponse;
import com.omgservers.service.module.pool.impl.operation.poolServer.selectPoolServer.SelectPoolServerOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetPoolServerMethodImpl implements GetPoolServerMethod {

    final SelectPoolServerOperation selectPoolServerOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<GetPoolServerResponse> getPoolServer(final GetPoolServerRequest request) {
        log.debug("Get pool server, request={}", request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shard -> {
                    final var poolId = request.getPoolId();
                    final var id = request.getId();
                    return pgPool.withTransaction(sqlConnection -> selectPoolServerOperation
                            .selectPoolServer(sqlConnection, shard.shard(), poolId, id));
                })
                .map(GetPoolServerResponse::new);
    }
}
