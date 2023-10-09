package com.omgservers.module.gateway.impl.service.messageService.impl.messageHandler;

import com.omgservers.dto.internal.SyncEventRequest;
import com.omgservers.model.assignedClient.AssignedClientModel;
import com.omgservers.model.event.body.ChangeRequestedEventBodyModel;
import com.omgservers.model.message.MessageModel;
import com.omgservers.model.message.MessageQualifierEnum;
import com.omgservers.model.message.body.ChangeMessageBodyModel;
import com.omgservers.module.gateway.GatewayModule;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetAssignedClientRequest;
import com.omgservers.module.gateway.impl.service.messageService.impl.MessageHandler;
import com.omgservers.module.system.SystemModule;
import com.omgservers.module.system.factory.EventModelFactory;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ChangeMessageHandlerImpl implements MessageHandler {

    final GatewayModule gatewayModule;
    final SystemModule systemModule;

    final EventModelFactory eventModelFactory;

    @Override
    public MessageQualifierEnum getQualifier() {
        return MessageQualifierEnum.CHANGE_MESSAGE;
    }

    @Override
    public Uni<Void> handle(Long connectionId, MessageModel message) {
        final var assignedClient = getAssignedClient(connectionId);
        final var tenantId = assignedClient.getTenantId();
        final var stageId = assignedClient.getStageId();
        final var userId = assignedClient.getUserId();
        final var playerId = assignedClient.getPlayerId();
        final var clientId = assignedClient.getClientId();

        final var eventBodyBuilder = ChangeRequestedEventBodyModel.builder()
                .tenantId(tenantId)
                .stageId(stageId)
                .userId(userId)
                .playerId(playerId)
                .clientId(clientId);

        final var messageBody = (ChangeMessageBodyModel) message.getBody();
        final var messageData = messageBody.getData();
        if (Objects.nonNull(messageData)) {
            final var eventBody = eventBodyBuilder.data(messageData).build();
            final var event = eventModelFactory.create(eventBody);
            final var syncEventRequest = new SyncEventRequest(event);
            return systemModule.getEventService().syncEvent(syncEventRequest)
                    .replaceWithVoid();
        } else {
            return Uni.createFrom().voidItem();
        }
    }

    AssignedClientModel getAssignedClient(Long connectionId) {
        final var request = new GetAssignedClientRequest(connectionId);
        final var response = gatewayModule.getConnectionService().getAssignedClient(request);
        return response.getAssignedClient();
    }
}
