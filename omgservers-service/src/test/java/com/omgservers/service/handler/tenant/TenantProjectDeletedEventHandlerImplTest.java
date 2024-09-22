package com.omgservers.service.handler.tenant;

import com.omgservers.schema.module.tenant.tenantProject.DeleteTenantProjectRequest;
import com.omgservers.service.event.body.module.tenant.TenantProjectDeletedEventBodyModel;
import com.omgservers.service.factory.system.EventModelFactory;
import com.omgservers.service.handler.tenant.testInterface.ProjectDeletedEventHandlerImplTestInterface;
import com.omgservers.service.module.tenant.impl.service.tenantService.testInterface.TenantServiceTestInterface;
import com.omgservers.testDataFactory.TestDataFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class TenantProjectDeletedEventHandlerImplTest extends Assertions {

    @Inject
    ProjectDeletedEventHandlerImplTestInterface projectDeletedEventHandler;

    @Inject
    TenantServiceTestInterface tenantService;

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

        final var deleteTenantProjectRequest = new DeleteTenantProjectRequest(tenantId, id);
        tenantService.deleteTenantProject(deleteTenantProjectRequest);

        final var eventBody = new TenantProjectDeletedEventBodyModel(tenantId, id);
        final var eventModel = eventModelFactory.create(eventBody);

        projectDeletedEventHandler.handle(eventModel);
        log.info("Retry");
        projectDeletedEventHandler.handle(eventModel);
    }
}