package com.omgservers.module.user.impl.operation.deleteClient;

import com.omgservers.model.player.PlayerConfigModel;
import com.omgservers.model.user.UserRoleEnum;
import com.omgservers.module.user.factory.ClientModelFactory;
import com.omgservers.module.user.factory.PlayerModelFactory;
import com.omgservers.module.user.factory.UserModelFactory;
import com.omgservers.module.user.impl.operation.upsertClient.UpsertClientOperation;
import com.omgservers.module.user.impl.operation.upsertPlayer.UpsertPlayerOperation;
import com.omgservers.module.user.impl.operation.upsertUser.UpsertUserOperation;
import com.omgservers.operation.generateId.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

@Slf4j
@QuarkusTest
class DeleteClientOperationTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    DeleteClientOperation deleteClientOperation;

    @Inject
    UpsertClientOperation insertClientOperation;

    @Inject
    UpsertUserOperation upsertUserOperation;

    @Inject
    UpsertPlayerOperation upsertPlayerOperation;

    @Inject
    UserModelFactory userModelFactory;

    @Inject
    PlayerModelFactory playerModelFactory;

    @Inject
    ClientModelFactory clientModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenUserPlayerClient_whenDeleteClient_thenDeleted() {
        final var shard = 0;
        final var user = userModelFactory.create(UserRoleEnum.PLAYER, "passwordhash");
        upsertUserOperation.upsertUser(TIMEOUT, pgPool, shard, user);
        final var player = playerModelFactory.create(user.getId(), stageId(), PlayerConfigModel.create());
        upsertPlayerOperation.upsertPlayer(TIMEOUT, pgPool, shard, player);
        final var client = clientModelFactory.create(player.getId(), URI.create("http://localhost:8080"), connectionId());
        final var clientId = client.getId();
        insertClientOperation.upsertClient(TIMEOUT, pgPool, shard, user.getId(), client);

        assertTrue(deleteClientOperation.deleteClient(TIMEOUT, pgPool, shard, user.getId(), clientId));
    }

    @Test
    void givenUnknownUuid_whenDeleteClient_thenSkip() {
        final var shard = 0;
        final var userId = generateIdOperation.generateId();
        final var id = generateIdOperation.generateId();

        assertFalse(deleteClientOperation.deleteClient(TIMEOUT, pgPool, shard, userId, id));
    }

    Long stageId() {
        return generateIdOperation.generateId();
    }

    Long connectionId() {
        return generateIdOperation.generateId();
    }
}