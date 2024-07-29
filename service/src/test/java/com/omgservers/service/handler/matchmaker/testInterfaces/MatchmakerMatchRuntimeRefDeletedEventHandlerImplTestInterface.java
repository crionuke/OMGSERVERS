package com.omgservers.service.handler.matchmaker.testInterfaces;

import com.omgservers.schema.event.EventModel;
import com.omgservers.service.handler.matchmaker.MatchmakerMatchRuntimeRefDeletedEventHandlerImpl;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class MatchmakerMatchRuntimeRefDeletedEventHandlerImplTestInterface {
    private static final long TIMEOUT = 1L;

    final MatchmakerMatchRuntimeRefDeletedEventHandlerImpl matchmakerMatchRuntimeRefDeletedEventHandler;

    public void handle(final EventModel event) {
        matchmakerMatchRuntimeRefDeletedEventHandler.handle(event)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
