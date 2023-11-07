package com.omgservers.service.handler;

import com.omgservers.model.client.ClientModel;
import com.omgservers.model.dto.matchmaker.SyncMatchmakerCommandRequest;
import com.omgservers.model.dto.matchmaker.SyncMatchmakerCommandResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeGrantResponse;
import com.omgservers.model.dto.runtime.FindRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.FindRuntimeGrantResponse;
import com.omgservers.model.dto.user.GetClientRequest;
import com.omgservers.model.dto.user.GetClientResponse;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.event.body.ClientDeletedEventBodyModel;
import com.omgservers.model.matchmakerCommand.body.DeleteClientMatchmakerCommandBodyModel;
import com.omgservers.service.factory.MatchmakerCommandModelFactory;
import com.omgservers.service.module.matchmaker.MatchmakerModule;
import com.omgservers.service.module.runtime.RuntimeModule;
import com.omgservers.service.module.system.impl.service.handlerService.impl.EventHandler;
import com.omgservers.service.module.user.UserModule;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientDeletedEventHandlerImpl implements EventHandler {

    final MatchmakerModule matchmakerModule;
    final RuntimeModule runtimeModule;
    final UserModule userModule;

    final MatchmakerCommandModelFactory matchmakerCommandModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.CLIENT_DELETED;
    }

    @Override
    public Uni<Boolean> handle(final EventModel event) {
        final var body = (ClientDeletedEventBodyModel) event.getBody();
        final var userId = body.getUserId();
        final var clientId = body.getId();

        return getDeletedClient(userId, clientId)
                .flatMap(client -> {
                    log.info("Client was deleted, client={}/{}", userId, clientId);

                    final var runtimeId = client.getDefaultRuntimeId();
                    final var matchmakerId = client.getDefaultMatchmakerId();
                    return deleteRuntimeGrantForClient(runtimeId, clientId)
                            .flatMap(wasGrantDeleted -> syncDeleteClientMatchmakerCommand(matchmakerId, clientId));

                })
                .replaceWith(true);
    }

    Uni<ClientModel> getDeletedClient(final Long userId, final Long clientId) {
        final var getClientServiceRequest = new GetClientRequest(userId, clientId, true);
        return userModule.getClientService().getClient(getClientServiceRequest)
                .map(GetClientResponse::getClient);
    }

    Uni<Boolean> deleteRuntimeGrantForClient(final Long runtimeId,
                                             final Long clientId) {
        final var findRuntimeGrantRequest = new FindRuntimeGrantRequest(runtimeId, clientId);
        return runtimeModule.getRuntimeService().findRuntimeGrant(findRuntimeGrantRequest)
                .map(FindRuntimeGrantResponse::getRuntimeGrant)
                .flatMap(runtimeGrant -> {
                    final var request = new DeleteRuntimeGrantRequest(runtimeId, clientId);
                    return runtimeModule.getRuntimeService().deleteRuntimeGrant(request)
                            .map(DeleteRuntimeGrantResponse::getDeleted);
                });
    }

    Uni<Boolean> syncDeleteClientMatchmakerCommand(final Long matchmakerId,
                                                   final Long clientId) {
        final var commandBody = new DeleteClientMatchmakerCommandBodyModel(clientId);
        final var commandModel = matchmakerCommandModelFactory.create(matchmakerId, commandBody);
        final var request = new SyncMatchmakerCommandRequest(commandModel);
        return matchmakerModule.getMatchmakerService().syncMatchmakerCommand(request)
                .map(SyncMatchmakerCommandResponse::getCreated);
    }
}

