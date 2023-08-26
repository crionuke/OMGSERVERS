package com.omgservers.application.module.userModule.impl.service.userHelpService.impl.method.respondClientHelpMethod;

import com.omgservers.application.module.gatewayModule.GatewayModule;
import com.omgservers.application.module.userModule.UserModule;
import com.omgservers.application.module.userModule.impl.service.userHelpService.request.RespondClientHelpRequest;
import com.omgservers.dto.gatewayModule.RespondMessageInternalRequest;
import com.omgservers.dto.userModule.GetClientRoutedRequest;
import com.omgservers.dto.userModule.GetClientInternalResponse;
import com.omgservers.model.client.ClientModel;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class RespondClientHelpMethodImpl implements RespondClientHelpMethod {

    final UserModule userModule;
    final GatewayModule gatewayModule;

    @Override
    public Uni<Void> respondClient(RespondClientHelpRequest request) {
        RespondClientHelpRequest.validateRespondClientServiceRequest(request);

        final var userId = request.getUserId();
        final var clientId = request.getClientId();

        return getClient(userId, clientId)
                .flatMap(client -> {
                    final var server = client.getServer();
                    final var connection = client.getConnectionId();
                    final var message = request.getMessage();
                    final var respondMessageInternalRequest =
                            new RespondMessageInternalRequest(server, connection, message);
                    return gatewayModule.getGatewayInternalService().respondMessage(respondMessageInternalRequest);
                });
    }

    Uni<ClientModel> getClient(Long userId, Long clientId) {
        final var getClientServiceRequest = new GetClientRoutedRequest(userId, clientId);
        return userModule.getClientInternalService().getClient(getClientServiceRequest)
                .map(GetClientInternalResponse::getClient);
    }
}
