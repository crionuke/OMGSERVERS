package com.omgservers.handler;

import com.omgservers.dto.gateway.AssignClientRequest;
import com.omgservers.dto.gateway.AssignClientResponse;
import com.omgservers.dto.gateway.RespondMessageRequest;
import com.omgservers.dto.gateway.RespondMessageResponse;
import com.omgservers.dto.runtime.SyncRuntimeCommandRequest;
import com.omgservers.dto.runtime.SyncRuntimeCommandResponse;
import com.omgservers.dto.tenant.FindStageVersionIdRequest;
import com.omgservers.dto.tenant.FindStageVersionIdResponse;
import com.omgservers.dto.tenant.SelectVersionRuntimeRequest;
import com.omgservers.dto.tenant.SelectVersionRuntimeResponse;
import com.omgservers.dto.tenant.ValidateStageSecretRequest;
import com.omgservers.dto.tenant.ValidateStageSecretResponse;
import com.omgservers.dto.user.SyncClientRequest;
import com.omgservers.dto.user.SyncPlayerRequest;
import com.omgservers.dto.user.SyncUserRequest;
import com.omgservers.model.assignedClient.AssignedClientModel;
import com.omgservers.model.client.ClientModel;
import com.omgservers.model.event.EventModel;
import com.omgservers.model.event.EventQualifierEnum;
import com.omgservers.model.event.body.SignUpRequestedEventBodyModel;
import com.omgservers.model.message.MessageQualifierEnum;
import com.omgservers.model.message.body.CredentialsMessageBodyModel;
import com.omgservers.model.player.PlayerConfigModel;
import com.omgservers.model.player.PlayerModel;
import com.omgservers.model.runtimeCommand.body.AddClientRuntimeCommandBodyModel;
import com.omgservers.model.user.UserModel;
import com.omgservers.model.user.UserRoleEnum;
import com.omgservers.model.versionRuntime.VersionRuntimeModel;
import com.omgservers.module.gateway.GatewayModule;
import com.omgservers.module.gateway.factory.MessageModelFactory;
import com.omgservers.module.runtime.RuntimeModule;
import com.omgservers.module.runtime.factory.RuntimeCommandModelFactory;
import com.omgservers.module.system.impl.service.handlerService.impl.EventHandler;
import com.omgservers.module.tenant.TenantModule;
import com.omgservers.module.user.UserModule;
import com.omgservers.module.user.factory.ClientModelFactory;
import com.omgservers.module.user.factory.PlayerModelFactory;
import com.omgservers.module.user.factory.UserModelFactory;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.security.SecureRandom;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SignUpRequestedEventHandlerImpl implements EventHandler {

    final GatewayModule gatewayModule;
    final RuntimeModule runtimeModule;
    final TenantModule tenantModule;
    final UserModule userModule;

    final RuntimeCommandModelFactory runtimeCommandModelFactory;
    final MessageModelFactory messageModelFactory;
    final ClientModelFactory clientModelFactory;
    final PlayerModelFactory playerModelFactory;
    final UserModelFactory userModelFactory;

    @Override
    public EventQualifierEnum getQualifier() {
        return EventQualifierEnum.SIGN_UP_REQUESTED;
    }

    @Override
    public Uni<Boolean> handle(EventModel event) {
        final var body = (SignUpRequestedEventBodyModel) event.getBody();
        final var server = body.getServer();
        final var connectionId = body.getConnectionId();
        final var tenantId = body.getTenantId();
        final var stageId = body.getStageId();
        final var secret = body.getSecret();

        //TODO: improve it
        final var password = String.valueOf(new SecureRandom().nextLong());

        return validateStageSecret(tenantId, stageId, secret)
                .flatMap(validateStageSecretResponse -> syncUser(password))
                .flatMap(user -> {
                    final var userId = user.getId();
                    return respondCredentials(server, connectionId, userId, password)
                            .flatMap(respondMessageResponse -> createPlayer(userId, tenantId, stageId))
                            .flatMap(player -> {
                                final var playerId = player.getId();
                                return findVersionId(tenantId, stageId)
                                        .flatMap(versionId -> selectVersionRuntime(tenantId, versionId)
                                                .flatMap(versionRuntime -> createClient(userId,
                                                        playerId,
                                                        server,
                                                        connectionId,
                                                        versionId,
                                                        versionRuntime.getRuntimeId())
                                                        .call(client -> syncAddClientRuntimeCommand(
                                                                versionRuntime.getRuntimeId(),
                                                                client))
                                                        .call(client -> assignClient(player, client)
                                                                .invoke(voidItem -> {
                                                                    log.info("User signed up, " +
                                                                                    "userId={}, " +
                                                                                    "clientId={}, " +
                                                                                    "tenantId={}, " +
                                                                                    "stageId={}, " +
                                                                                    "versionId={}",
                                                                            userId,
                                                                            client.getId(),
                                                                            tenantId,
                                                                            stageId,
                                                                            client.getVersionId());
                                                                })
                                                        )
                                                )
                                        );
                            });

                })
                .replaceWith(true);
    }

    Uni<ValidateStageSecretResponse> validateStageSecret(final Long tenantId, final Long stageId,
                                                         final String stageSecret) {
        final var request = new ValidateStageSecretRequest(tenantId, stageId, stageSecret);
        return tenantModule.getStageService().validateStageSecret(request);
    }

    Uni<UserModel> syncUser(final String password) {
        final var passwordHash = BcryptUtil.bcryptHash(password);
        final var user = userModelFactory.create(UserRoleEnum.PLAYER, passwordHash);
        final var request = new SyncUserRequest(user);
        return userModule.getUserService().syncUser(request)
                .replaceWith(user);
    }

    Uni<RespondMessageResponse> respondCredentials(URI server, Long connectionId, Long userId, String password) {
        final var body = new CredentialsMessageBodyModel(userId, password);
        final var message = messageModelFactory.create(MessageQualifierEnum.CREDENTIALS_MESSAGE, body);
        final var request = new RespondMessageRequest(server, connectionId, message);
        return gatewayModule.getGatewayService().respondMessage(request);
    }

    Uni<PlayerModel> createPlayer(Long userId, Long tenantId, Long stageId) {
        final var player = playerModelFactory.create(userId, tenantId, stageId, new PlayerConfigModel());
        final var syncPlayerRequest = new SyncPlayerRequest(player);
        return userModule.getPlayerService().syncPlayer(syncPlayerRequest)
                .replaceWith(player);
    }

    Uni<Long> findVersionId(final Long tenantId, final Long stageId) {
        final var request = new FindStageVersionIdRequest(tenantId, stageId);
        return tenantModule.getVersionService().findStageVersionId(request)
                .map(FindStageVersionIdResponse::getVersionId);
    }

    Uni<VersionRuntimeModel> selectVersionRuntime(final Long tenantId, final Long versionId) {
        final var request = new SelectVersionRuntimeRequest(tenantId,
                versionId,
                SelectVersionRuntimeRequest.Strategy.RANDOM);
        return tenantModule.getVersionService().selectVersionRuntime(request)
                .map(SelectVersionRuntimeResponse::getVersionRuntime);
    }

    Uni<ClientModel> createClient(final Long userId,
                                  final Long playerId,
                                  final URI server,
                                  final Long connectionId,
                                  final Long versionId,
                                  final Long defaultRuntimeId) {
        final var client = clientModelFactory.create(userId,
                playerId,
                server,
                connectionId,
                versionId,
                defaultRuntimeId);
        final var request = new SyncClientRequest(client);
        return userModule.getClientService().syncClient(request)
                .replaceWith(client);
    }

    Uni<Boolean> syncAddClientRuntimeCommand(final Long runtimeId,
                                             final ClientModel client) {
        final var clientId = client.getId();
        final var userId = client.getUserId();
        final var runtimeCommandBody = new AddClientRuntimeCommandBodyModel(userId, clientId);
        final var runtimeCommand = runtimeCommandModelFactory.create(runtimeId, runtimeCommandBody);
        final var syncRuntimeCommandShardedRequest = new SyncRuntimeCommandRequest(runtimeCommand);
        return runtimeModule.getRuntimeService().syncRuntimeCommand(syncRuntimeCommandShardedRequest)
                .map(SyncRuntimeCommandResponse::getCreated);
    }

    Uni<Void> assignClient(final PlayerModel player,
                           final ClientModel client) {
        final var tenantId = player.getTenantId();
        final var stageId = player.getStageId();
        final var userId = player.getUserId();
        final var playerId = player.getId();
        final var server = client.getServer();
        final var connectionId = client.getConnectionId();
        final var assignedClient = new AssignedClientModel(tenantId, stageId, userId, playerId, client.getId());
        final var request = new AssignClientRequest(server, connectionId, assignedClient);
        return gatewayModule.getGatewayService().assignClient(request)
                .map(AssignClientResponse::getAssigned)
                .replaceWithVoid();
    }
}
