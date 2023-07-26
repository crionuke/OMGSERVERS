package com.omgservers.application.module.luaModule.impl.service.luaRuntimeHelpService.impl.runtime.event;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class LuaPlayerSignedInEvent extends LuaEvent {

    final Long userId;
    final Long playerId;
    final Long clientId;

    public LuaPlayerSignedInEvent(Long userId, Long playerId, Long clientId) {
        super("player_signed_in");
        this.userId = userId;
        this.playerId = playerId;
        this.clientId = clientId;
        set("user_id", userId.toString());
        set("player_id", playerId.toString());
        set("client_id", clientId.toString());
    }
}
