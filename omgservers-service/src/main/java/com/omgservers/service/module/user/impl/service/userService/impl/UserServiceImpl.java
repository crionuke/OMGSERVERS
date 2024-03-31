package com.omgservers.service.module.user.impl.service.userService.impl;

import com.omgservers.model.dto.user.CreateTokenRequest;
import com.omgservers.model.dto.user.CreateTokenResponse;
import com.omgservers.model.dto.user.DeletePlayerRequest;
import com.omgservers.model.dto.user.DeletePlayerResponse;
import com.omgservers.model.dto.user.DeleteUserRequest;
import com.omgservers.model.dto.user.DeleteUserResponse;
import com.omgservers.model.dto.user.FindPlayerRequest;
import com.omgservers.model.dto.user.FindPlayerResponse;
import com.omgservers.model.dto.user.GetPlayerAttributesRequest;
import com.omgservers.model.dto.user.GetPlayerAttributesResponse;
import com.omgservers.model.dto.user.GetPlayerProfileRequest;
import com.omgservers.model.dto.user.GetPlayerProfileResponse;
import com.omgservers.model.dto.user.GetPlayerRequest;
import com.omgservers.model.dto.user.GetPlayerResponse;
import com.omgservers.model.dto.user.GetUserRequest;
import com.omgservers.model.dto.user.GetUserResponse;
import com.omgservers.model.dto.user.SyncPlayerRequest;
import com.omgservers.model.dto.user.SyncPlayerResponse;
import com.omgservers.model.dto.user.SyncUserRequest;
import com.omgservers.model.dto.user.SyncUserResponse;
import com.omgservers.model.dto.user.UpdatePlayerAttributesRequest;
import com.omgservers.model.dto.user.UpdatePlayerAttributesResponse;
import com.omgservers.model.dto.user.UpdatePlayerProfileRequest;
import com.omgservers.model.dto.user.UpdatePlayerProfileResponse;
import com.omgservers.service.module.user.impl.operation.getUserModuleClient.GetUserModuleClientOperation;
import com.omgservers.service.module.user.impl.operation.getUserModuleClient.UserModuleClient;
import com.omgservers.service.module.user.impl.service.userService.UserService;
import com.omgservers.service.module.user.impl.service.userService.impl.method.user.createToken.CreateTokenMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.deletePlayer.DeletePlayerMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.user.deleteUser.DeleteUserMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.findPlayer.FindPlayerMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.getPlayer.GetPlayerMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.getPlayerAttributes.GetPlayerAttributesMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.getPlayerProfile.GetPlayerProfileMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.user.getUser.GetUserMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.syncPlayer.SyncPlayerMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.user.syncUser.SyncUserMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.updatePlayerAttributes.UpdatePlayerAttributesMethod;
import com.omgservers.service.module.user.impl.service.userService.impl.method.player.updatePlayerProfile.UpdatePlayerProfileMethod;
import com.omgservers.service.operation.calculateShard.CalculateShardOperation;
import com.omgservers.service.operation.handleInternalRequest.HandleInternalRequestOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class UserServiceImpl implements UserService {

    final UpdatePlayerAttributesMethod updatePlayerAttributes;
    final GetPlayerAttributesMethod getPlayerAttributesMethod;
    final UpdatePlayerProfileMethod updatePlayerProfileMethod;
    final GetPlayerProfileMethod getPlayerProfileMethod;
    final DeletePlayerMethod deletePlayerMethod;
    final CreateTokenMethod createTokenMethod;
    final SyncPlayerMethod syncPlayerMethod;
    final FindPlayerMethod findPlayerMethod;
    final DeleteUserMethod deleteUserMethod;
    final GetPlayerMethod getPlayerMethod;
    final SyncUserMethod syncUserMethod;
    final GetUserMethod getUserMethod;

    final GetUserModuleClientOperation getUserModuleClientOperation;
    final HandleInternalRequestOperation handleInternalRequestOperation;
    final CalculateShardOperation calculateShardOperation;

    @Override
    public Uni<GetUserResponse> getUser(@Valid final GetUserRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::getUser,
                getUserMethod::getUser);
    }

    @Override
    public Uni<SyncUserResponse> syncUser(@Valid final SyncUserRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::syncUser,
                syncUserMethod::syncUser);
    }

    @Override
    public Uni<DeleteUserResponse> deleteUser(@Valid final DeleteUserRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::deleteUser,
                deleteUserMethod::deleteUser);
    }

    @Override
    public Uni<CreateTokenResponse> createToken(@Valid final CreateTokenRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::createToken,
                createTokenMethod::createToken);
    }

    @Override
    public Uni<GetPlayerResponse> getPlayer(@Valid final GetPlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::getPlayer,
                getPlayerMethod::getPlayer);
    }

    @Override
    public Uni<GetPlayerAttributesResponse> getPlayerAttributes(@Valid final GetPlayerAttributesRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::getPlayerAttributes,
                getPlayerAttributesMethod::getPlayerAttributes);
    }

    @Override
    public Uni<GetPlayerProfileResponse> getPlayerProfile(@Valid final GetPlayerProfileRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::getPlayerProfile,
                getPlayerProfileMethod::getPlayerProfile);
    }

    @Override
    public Uni<FindPlayerResponse> findPlayer(@Valid final FindPlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::findPlayer,
                findPlayerMethod::findPlayer);
    }

    @Override
    public Uni<SyncPlayerResponse> syncPlayer(@Valid final SyncPlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::syncPlayer,
                syncPlayerMethod::syncPlayer);
    }

    @Override
    public Uni<UpdatePlayerAttributesResponse> updatePlayerAttributes(
            @Valid final UpdatePlayerAttributesRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::updatePlayerAttributes,
                updatePlayerAttributes::updatePlayerAttributes);
    }

    @Override
    public Uni<UpdatePlayerProfileResponse> updatePlayerProfile(@Valid final UpdatePlayerProfileRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::updatePlayerProfile,
                updatePlayerProfileMethod::updatePlayerProfile);
    }

    @Override
    public Uni<DeletePlayerResponse> deletePlayer(@Valid final DeletePlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getUserModuleClientOperation::getClient,
                UserModuleClient::deletePlayer,
                deletePlayerMethod::deletePlayer);
    }
}
