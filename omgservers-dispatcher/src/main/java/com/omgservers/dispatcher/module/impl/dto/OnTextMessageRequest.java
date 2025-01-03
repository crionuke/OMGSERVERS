package com.omgservers.dispatcher.module.impl.dto;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnTextMessageRequest {

    @NotNull
    WebSocketConnection webSocketConnection;

    @NotNull
    String message;
}
