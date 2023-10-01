package com.omgservers.module.gateway.impl.service.messageService.impl.messageHandler;

import com.omgservers.dto.internal.FireEventRequest;
import com.omgservers.model.assignedClient.AssignedClientModel;
import com.omgservers.model.assignedRuntime.AssignedRuntimeModel;
import com.omgservers.model.event.body.MatchMessageReceivedEventBodyModel;
import com.omgservers.model.message.MessageModel;
import com.omgservers.model.message.MessageQualifierEnum;
import com.omgservers.model.message.body.MatchMessageBodyModel;
import com.omgservers.module.gateway.GatewayModule;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetAssignedClientRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetAssignedRuntimeRequest;
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
class MatchMessageHandlerImpl implements MessageHandler {

    final GatewayModule gatewayModule;
    final SystemModule systemModule;

    final EventModelFactory eventModelFactory;

    @Override
    public MessageQualifierEnum getQualifier() {
        return MessageQualifierEnum.MATCH_MESSAGE;
    }

    @Override
    public Uni<Void> handle(Long connectionId, MessageModel message) {
        final var assignedClient = getAssignedClient(connectionId);
        final var tenantId = assignedClient.getTenantId();
        final var stageId = assignedClient.getStageId();
        final var userId = assignedClient.getUserId();
        final var playerId = assignedClient.getPlayerId();
        final var clientId = assignedClient.getClientId();

        final var assignedRuntime = getAssignedRuntime(connectionId);
        final var runtimeId = assignedRuntime.getRuntimeId();

        final var eventBodyBuilder = MatchMessageReceivedEventBodyModel.builder()
                .tenantId(tenantId)
                .stageId(stageId)
                .userId(userId)
                .playerId(playerId)
                .clientId(clientId)
                .runtimeId(runtimeId);

        final var messageBody = (MatchMessageBodyModel) message.getBody();
        final var messageData = messageBody.getData();
        if (Objects.nonNull(messageData)) {
            final var eventBody = eventBodyBuilder.data(messageData).build();
            final var event = eventModelFactory.create(eventBody);
            final var fireEventRequest = new FireEventRequest(event);
            return systemModule.getEventService().fireEvent(fireEventRequest)
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

    AssignedRuntimeModel getAssignedRuntime(Long connectionId) {
        final var request = new GetAssignedRuntimeRequest(connectionId);
        final var response = gatewayModule.getConnectionService().getAssignedRuntime(request);
        return response.getAssignedRuntime();
    }
}
