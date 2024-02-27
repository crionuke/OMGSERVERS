package com.omgservers.service.module.runtime.impl.service.doService.impl.method.doKickClient;

import com.omgservers.model.dto.client.DeleteClientRequest;
import com.omgservers.model.dto.client.DeleteClientResponse;
import com.omgservers.model.dto.matchmaker.SyncMatchmakerCommandRequest;
import com.omgservers.model.dto.matchmaker.SyncMatchmakerCommandResponse;
import com.omgservers.model.dto.runtime.DoKickClientRequest;
import com.omgservers.model.dto.runtime.DoKickClientResponse;
import com.omgservers.model.dto.runtime.GetRuntimeRequest;
import com.omgservers.model.dto.runtime.GetRuntimeResponse;
import com.omgservers.model.matchmakerCommand.body.KickClientMatchmakerCommandBodyModel;
import com.omgservers.model.runtime.RuntimeModel;
import com.omgservers.service.exception.ServerSideForbiddenException;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.factory.MatchmakerCommandModelFactory;
import com.omgservers.service.module.client.ClientModule;
import com.omgservers.service.module.matchmaker.MatchmakerModule;
import com.omgservers.service.module.runtime.RuntimeModule;
import com.omgservers.service.module.runtime.impl.operation.hasRuntimeClient.HasRuntimeClientOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DoKickClientMethodImpl implements DoKickClientMethod {

    final MatchmakerModule matchmakerModule;
    final RuntimeModule runtimeModule;
    final ClientModule clientModule;

    final HasRuntimeClientOperation hasRuntimeClientOperation;
    final CheckShardOperation checkShardOperation;

    final MatchmakerCommandModelFactory matchmakerCommandModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<DoKickClientResponse> doKickClient(final DoKickClientRequest request) {
        log.debug("Do kick client, request={}", request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var runtimeId = request.getRuntimeId();
                    final var clientId = request.getClientId();
                    return pgPool.withTransaction(sqlConnection -> hasRuntimeClientOperation.hasRuntimeClient(
                                    sqlConnection,
                                    shardModel.shard(),
                                    runtimeId,
                                    clientId)
                            .flatMap(has -> {
                                if (has) {
                                    return doKickClient(runtimeId, clientId);
                                } else {
                                    throw new ServerSideForbiddenException(
                                            String.format("runtime client was not found, " +
                                                            "runtimeId=%s, clientId=%s",
                                                    runtimeId, clientId));
                                }
                            })
                    );
                })
                .replaceWith(new DoKickClientResponse());
    }

    Uni<Boolean> doKickClient(final Long runtimeId,
                              final Long clientId) {
        return getRuntime(runtimeId)
                .flatMap(runtime -> switch (runtime.getQualifier()) {
                    case LOBBY -> {
                        log.info("Do kick client from the lobby (server), clientId={}, runtimeId={}",
                                clientId, runtimeId);

                        yield deleteClient(clientId)
                                .replaceWithVoid();
                    }
                    case MATCH -> {
                        log.info("Do kick client from the match, clientId={}, runtimeId={}",
                                clientId, runtimeId);

                        final var matchmakerId = runtime.getConfig().getMatchConfig().getMatchmakerId();
                        final var matchId = runtime.getConfig().getMatchConfig().getMatchId();

                        yield syncKickClientMatchmakerCommand(matchmakerId, matchId, clientId);
                    }
                })
                .replaceWith(true);
    }

    Uni<RuntimeModel> getRuntime(final Long id) {
        final var request = new GetRuntimeRequest(id);
        return runtimeModule.getRuntimeService().getRuntime(request)
                .map(GetRuntimeResponse::getRuntime);
    }

    Uni<Boolean> deleteClient(final Long clientId) {
        final var request = new DeleteClientRequest(clientId);
        return clientModule.getClientService().deleteClient(request)
                .map(DeleteClientResponse::getDeleted);
    }

    Uni<Boolean> syncKickClientMatchmakerCommand(final Long matchmakerId,
                                                 final Long matchId,
                                                 final Long clientId) {
        final var commandBody = new KickClientMatchmakerCommandBodyModel(clientId, matchId);
        final var commandModel = matchmakerCommandModelFactory.create(matchmakerId, commandBody);
        final var request = new SyncMatchmakerCommandRequest(commandModel);
        return matchmakerModule.getMatchmakerService().syncMatchmakerCommand(request)
                .map(SyncMatchmakerCommandResponse::getCreated)
                .onFailure(ServerSideNotFoundException.class)
                .recoverWithItem(Boolean.FALSE);
    }
}
