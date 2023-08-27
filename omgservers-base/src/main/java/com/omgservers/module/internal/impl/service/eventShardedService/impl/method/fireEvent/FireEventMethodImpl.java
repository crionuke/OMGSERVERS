package com.omgservers.module.internal.impl.service.eventShardedService.impl.method.fireEvent;

import com.omgservers.module.internal.InternalModule;
import com.omgservers.Dispatcher;
import com.omgservers.module.internal.impl.operation.upsertEvent.UpsertEventOperation;
import com.omgservers.dto.internal.ChangeRequest;
import com.omgservers.dto.internal.ChangeResponse;
import com.omgservers.dto.internal.FireEventShardedRequest;
import com.omgservers.dto.internal.FireEventShardedResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class FireEventMethodImpl implements FireEventMethod {

    final InternalModule internalModule;

    final UpsertEventOperation upsertEventOperation;

    final Dispatcher dispatcher;
    final PgPool pgPool;

    @Override
    public Uni<FireEventShardedResponse> fireEvent(final FireEventShardedRequest request) {
        FireEventShardedRequest.validate(request);

        final var event = request.getEvent();
        return internalModule.getChangeService().change(new ChangeRequest(request,
                        (sqlConnection, shardModel) -> upsertEventOperation
                                .upsertEvent(sqlConnection, event)))
                .map(ChangeResponse::getResult)
                .invoke(inserted -> {
                    final var eventId = event.getId();
                    final var groupId = event.getGroupId();
                    dispatcher.addEvent(eventId, groupId);
                })
                .map(FireEventShardedResponse::new);
    }
}
