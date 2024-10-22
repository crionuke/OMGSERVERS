package com.omgservers.service.handler.impl.client;

import com.omgservers.schema.model.clientMatchmakerRef.ClientMatchmakerRefModel;
import com.omgservers.schema.model.message.MessageQualifierEnum;
import com.omgservers.schema.model.message.body.MatchmakerAssignmentMessageBodyDto;
import com.omgservers.schema.module.client.GetClientMatchmakerRefRequest;
import com.omgservers.schema.module.client.GetClientMatchmakerRefResponse;
import com.omgservers.schema.module.client.SyncClientMessageRequest;
import com.omgservers.schema.module.client.SyncClientMessageResponse;
import com.omgservers.service.event.EventModel;
import com.omgservers.service.event.EventQualifierEnum;
import com.omgservers.service.event.body.module.client.ClientMatchmakerRefCreatedEventBodyModel;
import com.omgservers.service.factory.client.ClientMessageModelFactory;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.module.client.ClientModule;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientMatchmakerRefCreatedEventHandlerImpl implements EventHandler {

    final ClientModule clientModule;

    final ClientMessageModelFactory clientMessageModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.CLIENT_MATCHMAKER_REF_CREATED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.debug("Handle event, {}", event);

        final var body = (ClientMatchmakerRefCreatedEventBodyModel) event.getBody();
        final var clientId = body.getClientId();
        final var id = body.getId();

        final var idempotencyKey = event.getId().toString();

        return getClientMatchmakerRef(clientId, id)
                .flatMap(clientMatchmakerRef -> {
                    log.info("Created, {}", clientMatchmakerRef);

                    final var matchmakerId = clientMatchmakerRef.getMatchmakerId();
                    return syncMatchmakerAssignmentMessage(clientId, matchmakerId, idempotencyKey);
                })
                .replaceWithVoid();
    }

    Uni<ClientMatchmakerRefModel> getClientMatchmakerRef(final Long clientId, final Long id) {
        final var request = new GetClientMatchmakerRefRequest(clientId, id);
        return clientModule.getService().getClientMatchmakerRef(request)
                .map(GetClientMatchmakerRefResponse::getClientMatchmakerRef);
    }

    Uni<Boolean> syncMatchmakerAssignmentMessage(final Long clientId,
                                                 final Long matchmakerId,
                                                 final String idempotencyKey) {
        final var messageBody = new MatchmakerAssignmentMessageBodyDto(matchmakerId);
        final var clientMessage = clientMessageModelFactory.create(clientId,
                MessageQualifierEnum.MATCHMAKER_ASSIGNMENT_MESSAGE,
                messageBody,
                idempotencyKey);
        final var request = new SyncClientMessageRequest(clientMessage);
        return clientModule.getService().syncClientMessageWithIdempotency(request)
                .map(SyncClientMessageResponse::getCreated);
    }
}

