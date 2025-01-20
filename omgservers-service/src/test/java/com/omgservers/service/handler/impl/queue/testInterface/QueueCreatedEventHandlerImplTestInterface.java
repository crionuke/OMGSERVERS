package com.omgservers.service.handler.impl.queue.testInterface;

import com.omgservers.service.event.EventModel;
import com.omgservers.service.handler.impl.pool.PoolContainerCreatedEventHandlerImpl;
import com.omgservers.service.handler.impl.queue.QueueCreatedEventHandlerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class QueueCreatedEventHandlerImplTestInterface {
    private static final long TIMEOUT = 10L;

    final QueueCreatedEventHandlerImpl handler;

    public void handle(final EventModel event) {
        handler.handle(event)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
