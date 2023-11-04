package com.omgservers.handler;

import com.omgservers.model.dto.matchmaker.SyncMatchmakerCommandRequest;
import com.omgservers.model.dto.matchmaker.SyncMatchmakerCommandResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeGrantResponse;
import com.omgservers.model.dto.runtime.FindRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.FindRuntimeGrantResponse;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.event.body.ClientDeletedEventBodyModel;
import com.omgservers.model.matchmakerCommand.body.DeleteClientMatchmakerCommandBodyModel;
import com.omgservers.module.matchmaker.MatchmakerModule;
import com.omgservers.factory.MatchmakerCommandModelFactory;
import com.omgservers.module.runtime.RuntimeModule;
import com.omgservers.module.system.impl.service.handlerService.impl.EventHandler;
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

    final MatchmakerCommandModelFactory matchmakerCommandModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.CLIENT_DELETED;
    }

    @Override
    public Uni<Boolean> handle(final EventModel event) {
        final var body = (ClientDeletedEventBodyModel) event.getBody();
        final var client = body.getClient();
        final var userId = client.getUserId();
        final var clientId = client.getId();
        final var runtimeId = client.getDefaultRuntimeId();
        final var matchmakerId = client.getDefaultMatchmakerId();

        log.info("Client was deleted, userId={}, clientId={}", userId, clientId);

        return deleteRuntimeGrantForClient(runtimeId, clientId)
                .flatMap(wasGrantDeleted -> syncDeleteClientMatchmakerCommand(matchmakerId, clientId))
                .replaceWith(true);
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

