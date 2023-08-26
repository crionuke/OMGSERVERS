package com.omgservers.application.module.userModule.impl.service.playerHelpService.impl.method.getOrCreatePlayerHelpMethod;

import com.omgservers.application.factory.PlayerModelFactory;
import com.omgservers.application.module.userModule.impl.service.playerHelpService.request.GetOrCreatePlayerHelpRequest;
import com.omgservers.application.module.userModule.impl.service.playerHelpService.response.GetOrCreatePlayerHelpResponse;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.PlayerInternalService;
import com.omgservers.base.operation.generateId.GenerateIdOperation;
import com.omgservers.dto.userModule.GetPlayerRoutedRequest;
import com.omgservers.dto.userModule.GetPlayerInternalResponse;
import com.omgservers.dto.userModule.SyncPlayerRoutedRequest;
import com.omgservers.exception.ServerSideNotFoundException;
import com.omgservers.model.player.PlayerConfigModel;
import com.omgservers.model.player.PlayerModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class GetOrCreatePlayerHelpMethodImpl implements GetOrCreatePlayerHelpMethod {

    final PlayerInternalService playerInternalService;

    final PlayerModelFactory playerModelFactory;
    final GenerateIdOperation generateIdOperation;

    @Override
    public Uni<GetOrCreatePlayerHelpResponse> getOrCreatePlayer(GetOrCreatePlayerHelpRequest request) {
        GetOrCreatePlayerHelpRequest.validate(request);

        final var userId = request.getUserId();
        final var stageId = request.getStageId();
        return getPlayer(userId, stageId)
                .map(player -> new GetOrCreatePlayerHelpResponse(false, player))
                .onFailure(ServerSideNotFoundException.class)
                .recoverWithUni(t -> syncPlayer(userId, stageId)
                        .map(player -> new GetOrCreatePlayerHelpResponse(true, player)));
    }

    Uni<PlayerModel> getPlayer(Long userId, Long stageId) {
        final var request = new GetPlayerRoutedRequest(userId, stageId);
        return playerInternalService.getPlayer(request)
                .map(GetPlayerInternalResponse::getPlayer);
    }

    Uni<PlayerModel> syncPlayer(Long userId, Long stageId) {
        final var player = playerModelFactory
                .create(userId, stageId, PlayerConfigModel.create());
        final var request = new SyncPlayerRoutedRequest(player);
        return playerInternalService.syncPlayer(request)
                .replaceWith(player);
    }
}
