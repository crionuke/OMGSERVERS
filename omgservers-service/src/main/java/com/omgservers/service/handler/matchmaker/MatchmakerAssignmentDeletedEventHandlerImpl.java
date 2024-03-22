package com.omgservers.service.handler.matchmaker;

import com.omgservers.model.clientMatchmakerRef.ClientMatchmakerRefModel;
import com.omgservers.model.dto.client.DeleteClientMatchmakerRefRequest;
import com.omgservers.model.dto.client.DeleteClientMatchmakerRefResponse;
import com.omgservers.model.dto.client.FindClientMatchmakerRefRequest;
import com.omgservers.model.dto.client.FindClientMatchmakerRefResponse;
import com.omgservers.model.dto.matchmaker.GetMatchmakerAssignmentRequest;
import com.omgservers.model.dto.matchmaker.GetMatchmakerAssignmentResponse;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.event.body.module.MatchmakerAssignmentDeletedEventBodyModel;
import com.omgservers.model.matchmakerAssignment.MatchmakerAssignmentModel;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.factory.ClientMatchmakerRefModelFactory;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.module.client.ClientModule;
import com.omgservers.service.module.matchmaker.MatchmakerModule;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class MatchmakerAssignmentDeletedEventHandlerImpl implements EventHandler {

    final MatchmakerModule matchmakerModule;
    final ClientModule clientModule;

    final ClientMatchmakerRefModelFactory clientMatchmakerRefModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.MATCHMAKER_ASSIGNMENT_DELETED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.debug("Handle event, {}", event);

        final var body = (MatchmakerAssignmentDeletedEventBodyModel) event.getBody();
        final var matchmakerId = body.getMatchmakerId();
        final var id = body.getId();

        return getMatchmakerAssignment(matchmakerId, id)
                .flatMap(matchmakerAssignment -> {
                    final var clientId = matchmakerAssignment.getClientId();

                    log.info("Matchmaker assignment was deleted, matchmakerAssignment={}/{}, clientId={}",
                            matchmakerId, id, clientId);

                    return findAndDeleteClientMatchmakerRef(clientId, matchmakerId);
                })
                .replaceWithVoid();
    }

    Uni<MatchmakerAssignmentModel> getMatchmakerAssignment(final Long matchmakerId, final Long id) {
        final var request = new GetMatchmakerAssignmentRequest(matchmakerId, id);
        return matchmakerModule.getMatchmakerService().getMatchmakerAssignment(request)
                .map(GetMatchmakerAssignmentResponse::getMatchmakerAssignment);
    }

    Uni<Void> findAndDeleteClientMatchmakerRef(final Long clientId, final Long matchmakerId) {
        return findClientMatchmakerRef(clientId, matchmakerId)
                .onFailure(ServerSideNotFoundException.class)
                .recoverWithNull()
                .onItem().ifNotNull().transformToUni(clientMatchmakerRef ->
                        deleteClientMatchmakerRef(clientId, clientMatchmakerRef.getId()))
                .replaceWithVoid();
    }

    Uni<ClientMatchmakerRefModel> findClientMatchmakerRef(final Long clientId, final Long matchmakerId) {
        final var request = new FindClientMatchmakerRefRequest(clientId, matchmakerId);
        return clientModule.getClientService().findClientMatchmakerRef(request)
                .map(FindClientMatchmakerRefResponse::getClientMatchmakerRef);
    }

    Uni<Boolean> deleteClientMatchmakerRef(final Long clientId, final Long id) {
        final var request = new DeleteClientMatchmakerRefRequest(clientId, id);
        return clientModule.getClientService().deleteClientMatchmakerRef(request)
                .map(DeleteClientMatchmakerRefResponse::getDeleted);
    }
}
