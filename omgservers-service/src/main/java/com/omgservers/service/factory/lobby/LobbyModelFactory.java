package com.omgservers.service.factory.lobby;

import com.omgservers.schema.model.lobby.LobbyModel;
import com.omgservers.service.operation.server.GenerateIdOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class LobbyModelFactory {

    final GenerateIdOperation generateIdOperation;

    public LobbyModel create(final Long tenantId,
                             final Long deploymentId) {
        final var id = generateIdOperation.generateId();
        final var idempotencyKey = generateIdOperation.generateStringId();
        return create(id, tenantId, deploymentId, idempotencyKey);
    }

    public LobbyModel create(final Long id,
                             final Long tenantId,
                             final Long deploymentId) {
        final var idempotencyKey = generateIdOperation.generateStringId();
        return create(id, tenantId, deploymentId, idempotencyKey);
    }

    public LobbyModel create(final Long tenantId,
                             final Long deploymentId,
                             final String idempotencyKey) {
        final var id = generateIdOperation.generateId();
        return create(id, tenantId, deploymentId, idempotencyKey);
    }

    public LobbyModel create(final Long id,
                             final Long tenantId,
                             final Long deploymentId,
                             final String idempotencyKey) {
        final var now = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        final var runtimeId = generateIdOperation.generateId();

        final var lobby = new LobbyModel();
        lobby.setId(id);
        lobby.setIdempotencyKey(idempotencyKey);
        lobby.setCreated(now);
        lobby.setModified(now);
        lobby.setTenantId(tenantId);
        lobby.setDeploymentId(deploymentId);
        lobby.setRuntimeId(runtimeId);
        lobby.setDeleted(Boolean.FALSE);
        return lobby;
    }
}
