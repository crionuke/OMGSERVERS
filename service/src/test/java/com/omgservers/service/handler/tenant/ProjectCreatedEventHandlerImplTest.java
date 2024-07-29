package com.omgservers.service.handler.tenant;

import com.omgservers.schema.event.body.module.tenant.ProjectCreatedEventBodyModel;
import com.omgservers.service.factory.system.EventModelFactory;
import com.omgservers.service.handler.tenant.testInterface.ProjectCreatedEventHandlerImplTestInterface;
import com.omgservers.testDataFactory.TestDataFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class ProjectCreatedEventHandlerImplTest extends Assertions {

    @Inject
    ProjectCreatedEventHandlerImplTestInterface projectCreatedEventHandler;

    @Inject
    EventModelFactory eventModelFactory;

    @Inject
    TestDataFactory testDataFactory;

    @Test
    void givenHandler_whenRetry_thenFinished() {
        final var tenant = testDataFactory.getTenantTestDataFactory().createTenant();
        final var project = testDataFactory.getTenantTestDataFactory().createProject(tenant);

        final var tenantId = project.getTenantId();
        final var id = project.getId();

        final var eventBody = new ProjectCreatedEventBodyModel(tenantId, id);
        final var eventModel = eventModelFactory.create(eventBody);

        projectCreatedEventHandler.handle(eventModel);
        log.info("Retry");
        projectCreatedEventHandler.handle(eventModel);
    }
}