package com.omgservers.module.script.impl.сontext.player;

import com.omgservers.module.script.impl.сontext.player.function.LuaPlayerGetAttributesFunctionFactory;
import com.omgservers.module.script.impl.сontext.player.function.LuaPlayerRespondFunctionFactory;
import com.omgservers.module.script.impl.сontext.player.function.LuaPlayerSetAttributesFunctionFactory;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LuaPlayerContextFactory {

    final LuaPlayerRespondFunctionFactory respondFunctionFactory;
    final LuaPlayerSetAttributesFunctionFactory setAttributeFunctionFactory;
    final LuaPlayerGetAttributesFunctionFactory getAttributeFunctionFactory;

    public LuaPlayerContext build(Long userId, Long playerId, Long clientId) {
        final var context = LuaPlayerContext.builder()
                .userId(userId)
                .playerId(playerId)
                .clientId(clientId)
                .respondFunction(respondFunctionFactory.build(userId, clientId))
                .setAttributesFunction(setAttributeFunctionFactory.build(userId, playerId))
                .getAttributesFunction(getAttributeFunctionFactory.build(userId, playerId))
                .build();

        return context;
    }
}
