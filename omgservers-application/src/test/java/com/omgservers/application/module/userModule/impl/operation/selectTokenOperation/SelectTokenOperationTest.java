package com.omgservers.application.module.userModule.impl.operation.selectTokenOperation;

import com.omgservers.model.token.TokenModel;
import com.omgservers.base.factory.UserModelFactory;
import com.omgservers.model.user.UserRoleEnum;
import com.omgservers.exception.ServerSideNotFoundException;
import com.omgservers.application.module.userModule.impl.operation.insertTokenOperation.InsertTokenOperation;
import com.omgservers.application.module.userModule.impl.operation.upsertUserOperation.UpsertUserOperation;
import com.omgservers.base.impl.operation.generateIdOperation.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@Slf4j
@QuarkusTest
class SelectTokenOperationTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    SelectTokenOperation selectTokenOperation;

    @Inject
    InsertTokenOperation insertTokenOperation;

    @Inject
    UpsertUserOperation upsertUserOperation;

    @Inject
    UserModelFactory userModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenUserToken_whenSelectToken_thenSelected() {
        final var shard = 0;
        final var user = userModelFactory.create(UserRoleEnum.PLAYER, "passwordhash");
        upsertUserOperation.upsertUser(TIMEOUT, pgPool, shard, user);
        final var tokenObject = insertTokenOperation.insertToken(TIMEOUT, pgPool, shard, user).getTokenObject();
        final var tokenUuid = tokenObject.getId();

        TokenModel tokenModel = selectTokenOperation.selectToken(TIMEOUT, pgPool, shard, tokenUuid);
        assertEquals(tokenObject.getUserId(), tokenModel.getUserId());
        assertEquals(tokenObject.getId(), tokenModel.getId());
    }

    @Test
    void givenUnknownUuid_whenSelectToken_thenServerSideNotFoundException() {
        final var shard = 0;
        final var tokenId = generateIdOperation.generateId();

        assertThrows(ServerSideNotFoundException.class, () -> selectTokenOperation
                .selectToken(TIMEOUT, pgPool, shard, tokenId));
    }
}