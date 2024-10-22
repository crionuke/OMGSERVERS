package com.omgservers.service.module.pool.impl.service.poolService.impl.method.poolRequest.syncPoolRequest;

import com.omgservers.schema.module.pool.poolRequest.SyncPoolRequestRequest;
import com.omgservers.schema.module.pool.poolRequest.SyncPoolRequestResponse;
import com.omgservers.schema.model.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.module.pool.impl.operation.pool.hasPool.HasPoolOperation;
import com.omgservers.service.module.pool.impl.operation.poolRequest.upsertPoolRequest.UpsertPoolRequestOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import com.omgservers.service.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncPoolRequestMethodImpl implements SyncPoolRequestMethod {

    final UpsertPoolRequestOperation upsertPoolRequestOperation;
    final ChangeWithContextOperation changeWithContextOperation;
    final CheckShardOperation checkShardOperation;
    final HasPoolOperation hasPoolOperation;

    @Override
    public Uni<SyncPoolRequestResponse> syncPoolRequest(
            final SyncPoolRequestRequest request) {
        log.debug("Requested, {}", request);

        final var shardKey = request.getRequestShardKey();
        final var poolRequest = request.getPoolRequest();
        final var poolId = poolRequest.getPoolId();

        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(shardKey))
                .flatMap(shardModel -> {
                    final var shard = shardModel.shard();
                    return changeWithContextOperation.<Boolean>changeWithContext(
                                    (context, sqlConnection) -> hasPoolOperation
                                            .hasPool(sqlConnection, shard, poolId)
                                            .flatMap(has -> {
                                                if (has) {
                                                    return upsertPoolRequestOperation
                                                            .upsertPoolRequest(
                                                                    context,
                                                                    sqlConnection,
                                                                    shard,
                                                                    poolRequest);
                                                } else {
                                                    throw new ServerSideNotFoundException(
                                                            ExceptionQualifierEnum.PARENT_NOT_FOUND,
                                                            "pool does not exist or was deleted, id=" + poolId);
                                                }
                                            }))
                            .map(ChangeContext::getResult);
                })
                .map(SyncPoolRequestResponse::new);
    }
}
