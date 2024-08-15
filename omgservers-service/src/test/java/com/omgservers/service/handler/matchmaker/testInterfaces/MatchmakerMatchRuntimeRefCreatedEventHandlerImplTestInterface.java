package com.omgservers.service.handler.matchmaker.testInterfaces;

import com.omgservers.service.event.EventModel;
import com.omgservers.service.handler.matchmaker.MatchmakerMatchRuntimeRefCreatedEventHandlerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class MatchmakerMatchRuntimeRefCreatedEventHandlerImplTestInterface {
    private static final long TIMEOUT = 1L;

    final MatchmakerMatchRuntimeRefCreatedEventHandlerImpl matchmakerMatchRuntimeRefCreatedEventHandlerImpl;

    public void handle(final EventModel event) {
        matchmakerMatchRuntimeRefCreatedEventHandlerImpl.handle(event)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
