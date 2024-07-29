package com.omgservers.service.handler.runtime.testInterface;

import com.omgservers.schema.event.EventModel;
import com.omgservers.service.handler.runtime.RuntimeAssignmentCreatedEventHandlerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class RuntimeAssignmentCreatedEventHandlerImplTestInterface {
    private static final long TIMEOUT = 1L;

    final RuntimeAssignmentCreatedEventHandlerImpl runtimeAssignmentCreatedEventHandler;

    public void handle(final EventModel event) {
        runtimeAssignmentCreatedEventHandler.handle(event)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
