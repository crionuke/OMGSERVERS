package com.omgservers.application.module.gatewayModule.impl.service.handlerHelpService.impl.messageHandler;

import com.omgservers.application.module.internalModule.InternalModule;
import com.omgservers.application.module.internalModule.impl.service.eventInternalService.request.FireEventInternalRequest;
import com.omgservers.application.module.internalModule.model.event.body.SignUpRequestedEventBodyModel;
import com.omgservers.application.module.gatewayModule.model.message.MessageModel;
import com.omgservers.application.module.gatewayModule.model.message.MessageQualifierEnum;
import com.omgservers.application.module.gatewayModule.model.message.body.SignUpMessageBodyModel;
import com.omgservers.application.module.gatewayModule.impl.service.handlerHelpService.impl.MessageHandler;
import com.omgservers.application.operation.getConfigOperation.GetConfigOperation;
import io.smallrye.mutiny.Uni;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SignUpMessageHandlerImpl implements MessageHandler {

    final InternalModule internalModule;

    final GetConfigOperation getConfigOperation;

    @Override
    public MessageQualifierEnum getQualifier() {
        return MessageQualifierEnum.SIGN_UP_MESSAGE;
    }

    @Override
    public Uni<Void> handle(final UUID connection, final MessageModel message) {
        final var messageBody = (SignUpMessageBodyModel) message.getBody();
        final var tenant = messageBody.getTenant();
        final var stage = messageBody.getStage();
        final var stageSecret = messageBody.getSecret();
        final var serverUri = getConfigOperation.getConfig().serverUri();

        final var event = SignUpRequestedEventBodyModel.createEvent(serverUri, connection, tenant, stage, stageSecret);
        final var request = new FireEventInternalRequest(event);
        return internalModule.getEventInternalService().fireEvent(request)
                .replaceWithVoid();
    }
}
