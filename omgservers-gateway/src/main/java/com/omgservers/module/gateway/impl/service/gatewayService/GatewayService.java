package com.omgservers.module.gateway.impl.service.gatewayService;

import com.omgservers.dto.gateway.AssignPlayerRequest;
import com.omgservers.dto.gateway.RespondMessageRequest;
import io.smallrye.mutiny.Uni;

import java.time.Duration;

public interface GatewayService {

    Uni<Void> respondMessage(RespondMessageRequest request);

    default void respondMessage(long timeout, RespondMessageRequest request) {
        respondMessage(request).await().atMost(Duration.ofSeconds(timeout));
    }

    Uni<Void> assignPlayer(AssignPlayerRequest request);

    default void assignPlayer(long timeout, AssignPlayerRequest request) {
        assignPlayer(request).await().atMost(Duration.ofSeconds(timeout));
    }
}
