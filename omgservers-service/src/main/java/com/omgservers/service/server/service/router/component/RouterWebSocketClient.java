package com.omgservers.service.server.service.router.component;

import com.omgservers.service.server.service.router.RouterService;
import com.omgservers.service.server.service.router.dto.CloseServerConnectionRequest;
import com.omgservers.service.server.service.router.dto.TransferClientBinaryMessageRequest;
import com.omgservers.service.server.service.router.dto.TransferClientTextMessageRequest;
import io.quarkus.websockets.next.CloseReason;
import io.quarkus.websockets.next.OnBinaryMessage;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnError;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocketClient;
import io.quarkus.websockets.next.WebSocketClientConnection;
import io.smallrye.mutiny.Uni;
import io.vertx.core.buffer.Buffer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@WebSocketClient(path = "/omgservers/v1/entrypoint/websocket/connection")
public class RouterWebSocketClient {

    final RouterService routerService;

    @OnClose
    public Uni<Void> onClose(final WebSocketClientConnection clientConnection,
                             final CloseReason closeReason) {
        final var request = new CloseServerConnectionRequest(clientConnection, closeReason);
        return routerService.closeServerConnection(request)
                .replaceWithVoid();
    }

    @OnError
    public Uni<Void> onError(final WebSocketClientConnection clientConnection, final Throwable throwable) {
        final var request = new CloseServerConnectionRequest(clientConnection,
                RouterWebSocketCloseReasons.INTERNAL_EXCEPTION_OCCURRED);
        return routerService.closeServerConnection(request)
                .replaceWithVoid();
    }

    @OnTextMessage
    public Uni<Void> onTextMessage(final WebSocketClientConnection clientConnection,
                                   final String message) {
        final var request = new TransferClientTextMessageRequest(clientConnection, message);
        return routerService.transferClientTextMessage(request)
                .replaceWithVoid();
    }

    @OnBinaryMessage
    public Uni<Void> onBinaryMessage(final WebSocketClientConnection clientConnection,
                                     final Buffer message) {
        final var request = new TransferClientBinaryMessageRequest(clientConnection, message);
        return routerService.transferClientBinaryMessage(request)
                .replaceWithVoid();
    }
}
