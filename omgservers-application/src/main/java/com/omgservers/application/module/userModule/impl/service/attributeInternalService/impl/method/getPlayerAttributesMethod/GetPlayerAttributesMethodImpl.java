package com.omgservers.application.module.userModule.impl.service.attributeInternalService.impl.method.getPlayerAttributesMethod;

import com.omgservers.application.module.userModule.impl.operation.selectPlayerAttributesOperation.SelectPlayerAttributesOperation;
import com.omgservers.base.operation.checkShard.CheckShardOperation;
import com.omgservers.dto.userModule.GetPlayerAttributesRoutedRequest;
import com.omgservers.dto.userModule.GetPlayerAttributesInternalResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetPlayerAttributesMethodImpl implements GetPlayerAttributesMethod {

    final SelectPlayerAttributesOperation selectPlayerAttributesOperation;
    final CheckShardOperation checkShardOperation;
    final PgPool pgPool;

    @Override
    public Uni<GetPlayerAttributesInternalResponse> getPlayerAttributes(GetPlayerAttributesRoutedRequest request) {
        GetPlayerAttributesRoutedRequest.validate(request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var playerId = request.getPlayerId();
                    return pgPool.withTransaction(sqlConnection -> selectPlayerAttributesOperation
                            .selectPlayerAttributes(sqlConnection, shardModel.shard(), playerId));
                })
                .map(GetPlayerAttributesInternalResponse::new);
    }
}
