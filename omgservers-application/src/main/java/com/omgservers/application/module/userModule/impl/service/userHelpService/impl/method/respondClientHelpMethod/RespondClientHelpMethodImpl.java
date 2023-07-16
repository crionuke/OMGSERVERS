package com.omgservers.application.module.userModule.impl.service.userHelpService.impl.method.respondClientHelpMethod;

import com.omgservers.application.module.gatewayModule.GatewayModule;
import com.omgservers.application.module.userModule.UserModule;
import com.omgservers.application.module.userModule.impl.service.userHelpService.request.RespondClientHelpRequest;
import com.omgservers.application.module.userModule.model.client.ClientModel;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.request.GetClientInternalRequest;
import com.omgservers.application.module.gatewayModule.impl.service.gatewayInternalService.request.RespondMessageInternalRequest;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.response.GetClientInternalResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class RespondClientHelpMethodImpl implements RespondClientHelpMethod {

    final UserModule userModule;
    final GatewayModule gatewayModule;

    @Override
    public Uni<Void> respondClient(RespondClientHelpRequest request) {
        RespondClientHelpRequest.validateRespondClientServiceRequest(request);

        final var user = request.getUser();
        final var clientUuid = request.getClient();

        return getClient(user, clientUuid)
                .flatMap(client -> {
                    final var server = client.getServer();
                    final var connection = client.getConnection();
                    final var message = request.getMessage();
                    final var respondMessageInternalRequest = new RespondMessageInternalRequest(server, connection, message);
                    return gatewayModule.getGatewayInternalService().respondMessage(respondMessageInternalRequest);
                });
    }

    Uni<ClientModel> getClient(UUID user, UUID client) {
        final var getClientServiceRequest = new GetClientInternalRequest(user, client);
        return userModule.getClientInternalService().getClient(getClientServiceRequest)
                .map(GetClientInternalResponse::getClient);
    }
}
