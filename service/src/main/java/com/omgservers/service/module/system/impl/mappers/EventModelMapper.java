package com.omgservers.service.module.system.impl.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.event.EventStatusEnum;
import com.omgservers.model.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideConflictException;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class EventModelMapper {

    final ObjectMapper objectMapper;

    public EventModel fromRow(final Row row) {
        final var event = new EventModel();
        event.setId(row.getLong("id"));
        event.setIdempotencyKey(row.getString("idempotency_key"));
        event.setCreated(row.getOffsetDateTime("created").toInstant());
        event.setModified(row.getOffsetDateTime("modified").toInstant());
        event.setDelayed(row.getOffsetDateTime("delayed").toInstant());
        final var qualifier = EventQualifierEnum.valueOf(row.getString("qualifier"));
        event.setQualifier(qualifier);
        try {
            final var body = objectMapper.readValue(row.getString("body"), qualifier.getBodyClass());
            event.setBody(body);
        } catch (IOException e) {
            throw new ServerSideConflictException(ExceptionQualifierEnum.DB_DATA_CORRUPTED,
                    "event body can't be parsed, event=" + event, e);
        }
        event.setStatus(EventStatusEnum.valueOf(row.getString("status")));
        event.setDeleted(row.getBoolean("deleted"));
        return event;
    }
}
