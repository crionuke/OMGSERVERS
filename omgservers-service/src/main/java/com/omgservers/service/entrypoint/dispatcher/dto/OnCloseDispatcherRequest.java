package com.omgservers.service.entrypoint.dispatcher.dto;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.websockets.next.CloseReason;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnCloseDispatcherRequest {

    @NotNull
    SecurityIdentity securityIdentity;

    @NotNull
    WebSocketConnection webSocketConnection;

    @NotNull
    CloseReason closeReason;
}
