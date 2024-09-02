package com.omgservers.service.handler.lobby;

import com.omgservers.schema.model.clientMessage.ClientMessageModel;
import com.omgservers.schema.model.lobby.LobbyModel;
import com.omgservers.schema.model.message.MessageQualifierEnum;
import com.omgservers.schema.model.message.body.DisconnectionReasonEnum;
import com.omgservers.schema.model.message.body.DisconnectionReasonMessageBodyModel;
import com.omgservers.schema.model.runtimeAssignment.RuntimeAssignmentModel;
import com.omgservers.schema.model.versionLobbyRef.VersionLobbyRefModel;
import com.omgservers.schema.module.client.DeleteClientRequest;
import com.omgservers.schema.module.client.DeleteClientResponse;
import com.omgservers.schema.module.client.SyncClientMessageRequest;
import com.omgservers.schema.module.client.SyncClientMessageResponse;
import com.omgservers.schema.module.lobby.GetLobbyRequest;
import com.omgservers.schema.module.lobby.GetLobbyResponse;
import com.omgservers.schema.module.runtime.DeleteRuntimeRequest;
import com.omgservers.schema.module.runtime.DeleteRuntimeResponse;
import com.omgservers.schema.module.runtime.ViewRuntimeAssignmentsRequest;
import com.omgservers.schema.module.runtime.ViewRuntimeAssignmentsResponse;
import com.omgservers.schema.module.tenant.DeleteVersionLobbyRefRequest;
import com.omgservers.schema.module.tenant.DeleteVersionLobbyRefResponse;
import com.omgservers.schema.module.tenant.FindVersionLobbyRefRequest;
import com.omgservers.schema.module.tenant.FindVersionLobbyRefResponse;
import com.omgservers.service.event.EventModel;
import com.omgservers.service.event.EventQualifierEnum;
import com.omgservers.service.event.body.module.lobby.LobbyDeletedEventBodyModel;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.factory.client.ClientMessageModelFactory;
import com.omgservers.service.handler.EventHandler;
import com.omgservers.service.module.client.ClientModule;
import com.omgservers.service.module.lobby.LobbyModule;
import com.omgservers.service.module.runtime.RuntimeModule;
import com.omgservers.service.module.tenant.TenantModule;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LobbyDeletedEventHandlerImpl implements EventHandler {

    final RuntimeModule runtimeModule;
    final TenantModule tenantModule;
    final ClientModule clientModule;
    final LobbyModule lobbyModule;

    final ClientMessageModelFactory clientMessageModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.LOBBY_DELETED;
    }

    @Override
    public Uni<Void> handle(final EventModel event) {
        log.debug("Handle event, {}", event);

        final var body = (LobbyDeletedEventBodyModel) event.getBody();
        final var lobbyId = body.getId();

        return getLobby(lobbyId)
                .flatMap(lobby -> {
                    log.info("Lobby was deleted, id={}", lobbyId);

                    final var runtimeId = lobby.getRuntimeId();
                    final var idempotencyKey = event.getId().toString();
                    return deleteLobbyClients(runtimeId, idempotencyKey)
                            .flatMap(voidItem -> findAndDeleteVersionLobbyRef(lobby))
                            .flatMap(voidItem -> deleteRuntime(lobby.getRuntimeId()));
                })
                .replaceWithVoid();
    }

    Uni<LobbyModel> getLobby(final Long id) {
        final var request = new GetLobbyRequest(id);
        return lobbyModule.getLobbyService().getLobby(request)
                .map(GetLobbyResponse::getLobby);
    }

    Uni<Void> deleteLobbyClients(final Long runtimeId, final String idempotencyKey) {
        return viewRuntimeAssignments(runtimeId)
                .flatMap(runtimeAssignments -> Multi.createFrom().iterable(runtimeAssignments)
                        .onItem().transformToUniAndConcatenate(runtimeAssignment -> {
                            final var clientId = runtimeAssignment.getClientId();
                            return syncDisconnectionMessage(clientId, idempotencyKey)
                                    .flatMap(created -> deleteClient(clientId));
                        })
                        .collect().asList()
                        .replaceWithVoid()
                );
    }

    Uni<List<RuntimeAssignmentModel>> viewRuntimeAssignments(final Long runtimeId) {
        final var request = new ViewRuntimeAssignmentsRequest(runtimeId);
        return runtimeModule.getRuntimeService().viewRuntimeAssignments(request)
                .map(ViewRuntimeAssignmentsResponse::getRuntimeAssignments);
    }

    Uni<Boolean> syncDisconnectionMessage(final Long clientId, final String idempotencyKey) {
        final var messageBody = new DisconnectionReasonMessageBodyModel(DisconnectionReasonEnum.INTERNAL_FAILURE);
        final var disconnectionMessage = clientMessageModelFactory.create(clientId,
                MessageQualifierEnum.DISCONNECTION_REASON_MESSAGE,
                messageBody,
                idempotencyKey);
        return syncClientMessage(disconnectionMessage);
    }

    Uni<Boolean> syncClientMessage(final ClientMessageModel clientMessage) {
        final var request = new SyncClientMessageRequest(clientMessage);
        return clientModule.getClientService().syncClientMessageWithIdempotency(request)
                .map(SyncClientMessageResponse::getCreated);
    }

    Uni<Boolean> deleteClient(final Long clientId) {
        final var request = new DeleteClientRequest(clientId);
        return clientModule.getClientService().deleteClient(request)
                .map(DeleteClientResponse::getDeleted);
    }

    Uni<Void> findAndDeleteVersionLobbyRef(LobbyModel lobby) {
        final var tenantId = lobby.getTenantId();
        final var versionId = lobby.getVersionId();
        final var lobbyId = lobby.getId();
        return findVersionLobbyRef(tenantId, versionId, lobbyId)
                .onFailure(ServerSideNotFoundException.class)
                .recoverWithNull()
                .onItem().ifNotNull().transformToUni(this::deleteVersionLobbyRef)
                .replaceWithVoid();
    }

    Uni<VersionLobbyRefModel> findVersionLobbyRef(final Long tenantId,
                                                  final Long versionId,
                                                  final Long lobbyId) {
        final var request = new FindVersionLobbyRefRequest(tenantId, versionId, lobbyId);
        return tenantModule.getVersionService().findVersionLobbyRef(request)
                .map(FindVersionLobbyRefResponse::getVersionLobbyRef);
    }

    Uni<Boolean> deleteVersionLobbyRef(VersionLobbyRefModel versionLobbyRef) {
        final var tenantId = versionLobbyRef.getTenantId();
        final var id = versionLobbyRef.getId();
        final var request = new DeleteVersionLobbyRefRequest(tenantId, id);
        return tenantModule.getVersionService().deleteVersionLobbyRef(request)
                .map(DeleteVersionLobbyRefResponse::getDeleted);
    }

    Uni<Boolean> deleteRuntime(final Long runtimeId) {
        final var request = new DeleteRuntimeRequest(runtimeId);
        return runtimeModule.getRuntimeService().deleteRuntime(request)
                .map(DeleteRuntimeResponse::getDeleted);
    }
}
