package com.omgservers.service.handler.lobby;

import com.omgservers.model.event.body.module.lobby.LobbyRuntimeRefCreatedEventBodyModel;
import com.omgservers.service.factory.system.EventModelFactory;
import com.omgservers.testDataFactory.TestDataFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class LobbyRuntimeRefCreatedEventHandlerImplTest extends Assertions {

    @Inject
    LobbyRuntimeRefCreatedEventHandlerImpl lobbyRuntimeRefCreatedEventHandler;

    @Inject
    EventModelFactory eventModelFactory;

    @Inject
    TestDataFactory testDataFactory;

    @Test
    void givenHandler_whenRetry_thenFinished() {
        final var tenant = testDataFactory.getTenantTestDataFactory().createTenant();
        final var project = testDataFactory.getTenantTestDataFactory().createProject(tenant);
        final var stage = testDataFactory.getTenantTestDataFactory().createStage(project);
        final var version = testDataFactory.getTenantTestDataFactory().createVersion(stage);
        final var lobby = testDataFactory.getLobbyTestDataFactory().createLobby(version);
        final var lobbyRuntime = testDataFactory.getRuntimeTestDataFactory()
                .createLobbyRuntime(tenant, version, lobby);
        final var lobbyRuntimeRef = testDataFactory.getLobbyTestDataFactory()
                .createLobbyRuntimeRef(lobby, lobbyRuntime);

        final var lobbyId = lobbyRuntimeRef.getLobbyId();
        final var id = lobbyRuntimeRef.getId();

        final var eventBody = new LobbyRuntimeRefCreatedEventBodyModel(lobbyId, id);
        final var eventModel = eventModelFactory.create(eventBody);

        lobbyRuntimeRefCreatedEventHandler.handle(eventModel);
        log.info("Retry");
        lobbyRuntimeRefCreatedEventHandler.handle(eventModel);
    }
}