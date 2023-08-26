package com.omgservers.base.factory;

import com.omgservers.base.operation.generateId.GenerateIdOperation;
import com.omgservers.model.event.EventBodyModel;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventStatusEnum;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class EventModelFactory {

    final GenerateIdOperation generateIdOperation;

    public EventModel create(EventBodyModel body) {
        final var id = generateIdOperation.generateId();
        return create(id, body);
    }

    public EventModel create(Long id, EventBodyModel body) {
        Instant now = Instant.now();

        final var event = new EventModel();
        event.setId(id);
        event.setCreated(now);
        event.setModified(now);
        event.setGroupId(body.getGroupId());
        event.setQualifier(body.getQualifier());
        event.setBody(body);
        event.setStatus(EventStatusEnum.NEW);
        return event;
    }
}
