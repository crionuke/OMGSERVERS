package com.omgservers.module.runtime.operation;

import com.omgservers.exception.ServerSideConflictException;
import com.omgservers.model.runtime.RuntimeConfigModel;
import com.omgservers.model.runtime.RuntimeTypeEnum;
import com.omgservers.model.runtimeGrant.RuntimeGrantTypeEnum;
import com.omgservers.factory.RuntimeGrantModelFactory;
import com.omgservers.factory.RuntimeModelFactory;
import com.omgservers.module.runtime.impl.operation.upsertRuntime.UpsertRuntimeOperation;
import com.omgservers.module.runtime.impl.operation.upsertRuntimeGrant.UpsertRuntimeGrantOperation;
import com.omgservers.operation.generateId.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class UpsertRuntimeGrantOperationTest extends Assertions {
    private static final long TIMEOUT = 1L;

    @Inject
    UpsertRuntimeGrantOperation upsertRuntimeGrantOperation;

    @Inject
    UpsertRuntimeOperation upsertRuntimeOperation;

    @Inject
    RuntimeModelFactory runtimeModelFactory;

    @Inject
    RuntimeGrantModelFactory runtimeGrantModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenRuntimeGrant_whenUpsertRuntimeGrant_thenTrue() {
        final var shard = 0;
        final var runtime = runtimeModelFactory.create(tenantId(), versionId(), RuntimeTypeEnum.MATCH, new RuntimeConfigModel());
        upsertRuntimeOperation.upsertRuntime(TIMEOUT, pgPool, shard, runtime);

        final var runtimeGrant = runtimeGrantModelFactory
                .create(runtime.getId(), shardKey(), entityId(), RuntimeGrantTypeEnum.MATCH_CLIENT);
        assertTrue(upsertRuntimeGrantOperation.upsertRuntimeGrant(TIMEOUT, pgPool, shard, runtimeGrant));
    }

    @Test
    void givenRuntimeGrant_whenUpsertRuntimeGrantAgain_thenFalse() {
        final var shard = 0;
        final var runtime = runtimeModelFactory.create(tenantId(), versionId(), RuntimeTypeEnum.MATCH, new RuntimeConfigModel());
        upsertRuntimeOperation.upsertRuntime(TIMEOUT, pgPool, shard, runtime);

        final var runtimeGrant = runtimeGrantModelFactory
                .create(runtime.getId(), shardKey(), entityId(), RuntimeGrantTypeEnum.MATCH_CLIENT);
        upsertRuntimeGrantOperation.upsertRuntimeGrant(TIMEOUT, pgPool, shard, runtimeGrant);

        assertFalse(upsertRuntimeGrantOperation.upsertRuntimeGrant(TIMEOUT, pgPool, shard, runtimeGrant));
    }

    @Test
    void givenUnknownIds_whenUpsertRuntimeGrant_thenException() {
        final var shard = 0;
        final var runtimeGrant = runtimeGrantModelFactory
                .create(runtimeId(), shardKey(), entityId(), RuntimeGrantTypeEnum.MATCH_CLIENT);
        final var exception = assertThrows(ServerSideConflictException.class, () -> upsertRuntimeGrantOperation
                .upsertRuntimeGrant(TIMEOUT, pgPool, shard, runtimeGrant));
    }

    Long tenantId() {
        return generateIdOperation.generateId();
    }

    Long stageId() {
        return generateIdOperation.generateId();
    }

    Long versionId() {
        return generateIdOperation.generateId();
    }

    Long matchmakerId() {
        return generateIdOperation.generateId();
    }

    Long matchId() {
        return generateIdOperation.generateId();
    }

    Long runtimeId() {
        return generateIdOperation.generateId();
    }

    Long shardKey() {
        return generateIdOperation.generateId();
    }

    Long entityId() {
        return generateIdOperation.generateId();
    }
}