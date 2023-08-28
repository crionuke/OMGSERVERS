package com.omgservers.module.context.impl.service.handlerService.impl.method.handleHandleIncomingRuntimeCommand;

import com.omgservers.dto.handler.HandleHandleIncomingRuntimeCommandRequest;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class HandleHandleIncomingRuntimeCommandMethodImpl implements HandleHandleIncomingRuntimeCommandMethod {

    @Override
    public Uni<Void> handleHandleIncomingRuntimeCommand(HandleHandleIncomingRuntimeCommandRequest request) {
        HandleHandleIncomingRuntimeCommandRequest.validate(request);
        // TODO: implement
        return Uni.createFrom().voidItem();
    }
}
