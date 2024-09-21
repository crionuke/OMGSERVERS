package com.omgservers.service.handler.tenant.testInterface;

import com.omgservers.service.event.EventModel;
import com.omgservers.service.handler.tenant.TenantLobbyRequestDeletedEventHandlerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class VersionLobbyRequestDeletedEventHandlerImplTestInterface {
    private static final long TIMEOUT = 1L;

    final TenantLobbyRequestDeletedEventHandlerImpl versionLobbyRequestDeletedEventHandler;

    public void handle(final EventModel event) {
        versionLobbyRequestDeletedEventHandler.handle(event)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
