package com.omgservers.service.handler.internal;

import com.omgservers.service.event.body.internal.VersionDeploymentRequestedEventBodyModel;
import com.omgservers.service.factory.system.EventModelFactory;
import com.omgservers.service.handler.internal.testInterface.VersionDeploymentRequestedEventHandlerImplTestInterface;
import com.omgservers.testDataFactory.TestDataFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class VersionDeploymentRequestedEventHandlerImplTest extends Assertions {

    @Inject
    VersionDeploymentRequestedEventHandlerImplTestInterface versionDeploymentRequestedEventHandlerImplTestInterface;

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

        final var eventBody = new VersionDeploymentRequestedEventBodyModel(tenant.getId(), version.getId());
        final var eventModel = eventModelFactory.create(eventBody);

        versionDeploymentRequestedEventHandlerImplTestInterface.handle(eventModel);
        log.info("Retry");
        versionDeploymentRequestedEventHandlerImplTestInterface.handle(eventModel);
    }
}