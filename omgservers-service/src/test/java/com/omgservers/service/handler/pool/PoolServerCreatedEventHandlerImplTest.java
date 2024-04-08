package com.omgservers.service.handler.pool;

import com.omgservers.model.event.body.module.pool.PoolServerCreatedEventBodyModel;
import com.omgservers.service.factory.system.EventModelFactory;
import com.omgservers.service.handler.pool.testInterface.PoolServerCreatedEventHandlerImplTestInterface;
import com.omgservers.testDataFactory.TestDataFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class PoolServerCreatedEventHandlerImplTest extends Assertions {

    @Inject
    PoolServerCreatedEventHandlerImplTestInterface poolServerCreatedEventHandler;

    @Inject
    EventModelFactory eventModelFactory;

    @Inject
    TestDataFactory testDataFactory;

    @Test
    void givenHandler_whenRetry_thenFinished() {
        final var root = testDataFactory.getRootTestDataFactory().createRoot();
        final var defaultPool = testDataFactory.getPoolTestDataFactory().createDefaultPool(root);
        final var poolServer = testDataFactory.getPoolTestDataFactory().createPoolServer(defaultPool);

        final var poolId = poolServer.getPoolId();
        final var id = poolServer.getId();

        final var eventBody = new PoolServerCreatedEventBodyModel(poolId, id);
        final var eventModel = eventModelFactory.create(eventBody);

        poolServerCreatedEventHandler.handle(eventModel);
        log.info("Retry");
        poolServerCreatedEventHandler.handle(eventModel);
    }
}