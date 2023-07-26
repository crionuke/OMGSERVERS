package com.omgservers.application.module.userModule.impl.operation.insertClientOperation;

import com.omgservers.application.module.userModule.model.client.ClientModelFactory;
import com.omgservers.application.module.userModule.model.player.PlayerConfigModel;
import com.omgservers.application.module.userModule.model.player.PlayerModelFactory;
import com.omgservers.application.module.userModule.model.user.UserModelFactory;
import com.omgservers.application.module.userModule.model.user.UserRoleEnum;
import com.omgservers.application.exception.ServerSideConflictException;
import com.omgservers.application.exception.ServerSideNotFoundException;
import com.omgservers.application.module.userModule.impl.operation.upsertPlayerOperation.UpsertPlayerOperation;
import com.omgservers.application.module.userModule.impl.operation.upsertUserOperation.UpsertUserOperation;
import com.omgservers.application.operation.generateIdOperation.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.net.URI;
import java.util.UUID;

@Slf4j
@QuarkusTest
class InsertClientOperationTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    InsertClientOperation insertClientOperation;

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
    void givenUserPlayer_whenInsertClient_thenInserted() {
        final var shard = 0;
        final var user = userModelFactory.create(UserRoleEnum.PLAYER, "passwordhash");
        upsertUserOperation.upsertUser(TIMEOUT, pgPool, shard, user);
        final var player = playerModelFactory.create(user.getId(), stageId(), PlayerConfigModel.create());
        final var playerUuid = player.getId();
        upsertPlayerOperation.upsertPlayer(TIMEOUT, pgPool, shard, player);

        final var client = clientModelFactory.create(playerUuid, URI.create("http://localhost:8080"), connectionId());
        insertClientOperation.insertClient(TIMEOUT, pgPool, shard, client);
    }

    @Test
    void givenClient_whenInsertClientAgain_thenServerSideConflictException() {
        final var shard = 0;
        final var user = userModelFactory.create(UserRoleEnum.PLAYER, "passwordhash");
        upsertUserOperation.upsertUser(TIMEOUT, pgPool, shard, user);
        final var player = playerModelFactory.create(user.getId(), stageId(), PlayerConfigModel.create());
        final var playerUuid = player.getId();
        upsertPlayerOperation.upsertPlayer(TIMEOUT, pgPool, shard, player);
        final var client = clientModelFactory.create(playerUuid, URI.create("http://localhost:8080"), connectionId());
        insertClientOperation.insertClient(TIMEOUT, pgPool, shard, client);

        final var exception = assertThrows(ServerSideConflictException.class, () ->
                insertClientOperation.insertClient(TIMEOUT, pgPool, shard, client));
        log.info("Exception: {}", exception.getMessage());
    }

    @Test
    void whenInsertClientWithUnknownPlayerUuid_thenServerSideNotFoundException() {
        final var shard = 0;
        final var client = clientModelFactory.create(playerId(), URI.create("http://localhost:8080"), connectionId());
        final var exception = assertThrows(ServerSideNotFoundException.class, () ->
                insertClientOperation.insertClient(TIMEOUT, pgPool, shard, client));
        log.info("Exception: {}", exception.getMessage());
    }

    long playerId() {
        return generateIdOperation.generateId();
    }

    long connectionId() {
        return generateIdOperation.generateId();
    }

    long stageId() {
        return generateIdOperation.generateId();
    }
}