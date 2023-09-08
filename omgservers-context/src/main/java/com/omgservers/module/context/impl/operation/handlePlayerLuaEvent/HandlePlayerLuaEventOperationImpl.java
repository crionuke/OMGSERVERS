package com.omgservers.module.context.impl.operation.handlePlayerLuaEvent;

import com.omgservers.exception.ServerSideConflictException;
import com.omgservers.module.context.impl.luaEvent.LuaEvent;
import com.omgservers.module.context.impl.service.contextService.impl.cache.LuaInstanceCache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class HandlePlayerLuaEventOperationImpl implements HandlePlayerLuaEventOperation {

    final LuaInstanceCache cache;

    @Override
    public Uni<Boolean> handlePlayerLuaEvent(final Long clientId, final LuaEvent luaEvent) {
        final var cacheKey = clientId;
        return Uni.createFrom().item(cache.getValue(cacheKey))
                .onItem().ifNull().failWith(new ServerSideConflictException("player lua instance was not created for, " +
                        "clientId=" + clientId))
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .map(luaInstance -> luaInstance.handleEvent(luaEvent));
    }
}
