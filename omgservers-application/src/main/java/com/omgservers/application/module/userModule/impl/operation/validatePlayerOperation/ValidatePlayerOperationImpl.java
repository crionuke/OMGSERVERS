package com.omgservers.application.module.userModule.impl.operation.validatePlayerOperation;

import com.omgservers.application.exception.ServerSideBadRequestException;
import com.omgservers.application.module.userModule.model.player.PlayerModel;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
class ValidatePlayerOperationImpl implements ValidatePlayerOperation {

    @Override
    public PlayerModel validatePlayer(PlayerModel player) {
        if (player == null) {
            throw new IllegalArgumentException("mode is null");
        }

        var config = player.getConfig();

        Map<String, Boolean> results = new HashMap<>();

        // TODO: validate mode

        var valid = results.values().stream().allMatch(Boolean.TRUE::equals);
        if (valid) {
            log.info("Player is valid, player={}", player);
            return player;
        } else {
            throw new ServerSideBadRequestException(String.format("bad player, player=%s", player));
        }
    }
}
