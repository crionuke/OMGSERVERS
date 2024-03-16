package com.omgservers.service.operation.upsertEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.model.event.EventModel;
import com.omgservers.service.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideBadRequestException;
import com.omgservers.service.factory.LogModelFactory;
import com.omgservers.service.operation.changeObject.ChangeObjectOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertEventOperationImpl implements UpsertEventOperation {

    final ChangeObjectOperation changeObjectOperation;

    final LogModelFactory logModelFactory;
    final ObjectMapper objectMapper;

    @Override
    public Uni<Boolean> upsertEvent(final ChangeContext<?> changeContext,
                                    final SqlConnection sqlConnection,
                                    final EventModel event) {
        return changeObjectOperation.changeObject(
                        changeContext, sqlConnection, 0,
                        """
                                insert into system.tab_event(
                                    id, idempotency_key, created, modified, delayed, qualifier, body, status, deleted)
                                values($1, $2, $3, $4, $5, $6, $7, $8, $9)
                                on conflict (id) do
                                nothing
                                """,
                        List.of(
                                event.getId(),
                                event.getIdempotencyKey(),
                                event.getCreated().atOffset(ZoneOffset.UTC),
                                event.getModified().atOffset(ZoneOffset.UTC),
                                event.getDelayed().atOffset(ZoneOffset.UTC),
                                event.getQualifier(),
                                getBodyString(event),
                                event.getStatus(),
                                event.getDeleted()),
                        () -> null,
                        () -> null
                )
                .invoke(inserted -> {
                    if (inserted) {
                        changeContext.add(event);
                    }
                });
    }

    String getBodyString(final EventModel event) {
        try {
            return objectMapper.writeValueAsString(event.getBody());
        } catch (IOException e) {
            throw new ServerSideBadRequestException(ExceptionQualifierEnum.OBJECT_WRONG, e.getMessage(), e);
        }
    }
}
