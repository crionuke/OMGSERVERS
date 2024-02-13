package com.omgservers.service.module.runtime.impl.service.doService.impl.method.doSetAttributes;

import com.omgservers.model.dto.runtime.DoSetAttributesRequest;
import com.omgservers.model.dto.runtime.DoSetAttributesResponse;
import com.omgservers.model.dto.user.UpdatePlayerAttributesRequest;
import com.omgservers.model.dto.user.UpdatePlayerAttributesResponse;
import com.omgservers.model.player.PlayerAttributesModel;
import com.omgservers.service.exception.ServerSideBadRequestException;
import com.omgservers.service.module.client.ClientModule;
import com.omgservers.service.module.runtime.impl.operation.hasRuntimeClient.HasRuntimeClientOperation;
import com.omgservers.service.module.user.UserModule;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DoSetAttributesMethodImpl implements DoSetAttributesMethod {

    final ClientModule clientModule;
    final UserModule userModule;

    final HasRuntimeClientOperation hasRuntimeClientOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<DoSetAttributesResponse> doSetAttributes(final DoSetAttributesRequest request) {
        log.debug("Do set attributes, request={}", request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var runtimeId = request.getRuntimeId();
                    final var clientId = request.getClientId();
                    final var attributes = request.getAttributes();
                    return pgPool.withTransaction(sqlConnection -> hasRuntimeClientOperation.hasRuntimeClient(
                                    sqlConnection,
                                    shardModel.shard(),
                                    runtimeId,
                                    clientId)
                            .flatMap(has -> {
                                if (has) {
                                    return doSetAttributes(clientId, attributes);
                                } else {
                                    throw new ServerSideBadRequestException(
                                            String.format("runtime client was not found, " +
                                                            "runtimeId=%s, clientId=%s",
                                                    runtimeId, clientId));
                                }
                            })
                    );
                })
                .replaceWith(new DoSetAttributesResponse(true));
    }

    Uni<Boolean> doSetAttributes(final Long clientId,
                                 final PlayerAttributesModel attributes) {
        return clientModule.getShortcutService().getClient(clientId)
                .flatMap(client -> {
                    final var userId = client.getUserId();
                    final var playerId = client.getPlayerId();
                    return updatePlayerAttributes(userId, playerId, attributes);
                })
                .replaceWith(true);
    }

    Uni<Boolean> updatePlayerAttributes(final Long userId,
                                        final Long playerId,
                                        final PlayerAttributesModel attributes) {
        final var request = new UpdatePlayerAttributesRequest(userId, playerId, attributes);
        return userModule.getPlayerService().updatePlayerAttributes(request)
                .map(UpdatePlayerAttributesResponse::getUpdated);
    }
}
