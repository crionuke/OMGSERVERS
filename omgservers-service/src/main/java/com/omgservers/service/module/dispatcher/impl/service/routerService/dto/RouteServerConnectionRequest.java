package com.omgservers.service.module.dispatcher.impl.service.routerService.dto;

import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteServerConnectionRequest {

    @NotNull
    WebSocketConnection serverConnection;

    @NotNull
    URI serverUri;
}
