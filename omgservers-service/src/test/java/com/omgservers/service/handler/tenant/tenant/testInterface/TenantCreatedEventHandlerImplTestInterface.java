package com.omgservers.service.handler.tenant.tenant.testInterface;

import com.omgservers.model.event.EventModel;
import com.omgservers.service.handler.tenant.tenant.TenantCreatedEventHandlerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class TenantCreatedEventHandlerImplTestInterface {
    private static final long TIMEOUT = 1L;

    final TenantCreatedEventHandlerImpl tenantCreatedEventHandler;

    public void handle(final EventModel event) {
        tenantCreatedEventHandler.handle(event)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
