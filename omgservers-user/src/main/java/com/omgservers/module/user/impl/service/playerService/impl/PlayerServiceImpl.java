package com.omgservers.module.user.impl.service.playerService.impl;

import com.omgservers.dto.user.DeletePlayerRequest;
import com.omgservers.dto.user.DeletePlayerResponse;
import com.omgservers.dto.user.FindPlayerRequest;
import com.omgservers.dto.user.FindPlayerResponse;
import com.omgservers.dto.user.GetPlayerRequest;
import com.omgservers.dto.user.GetPlayerResponse;
import com.omgservers.dto.user.SyncPlayerRequest;
import com.omgservers.dto.user.SyncPlayerResponse;
import com.omgservers.module.user.impl.operation.getUserModuleClient.GetUserModuleClientOperation;
import com.omgservers.module.user.impl.operation.getUserModuleClient.UserModuleClient;
import com.omgservers.module.user.impl.service.playerService.PlayerService;
import com.omgservers.module.user.impl.service.playerService.impl.method.deletePlayer.DeletePlayerMethod;
import com.omgservers.module.user.impl.service.playerService.impl.method.findPlayer.FindPlayerMethod;
import com.omgservers.module.user.impl.service.playerService.impl.method.getPlayer.GetPlayerMethod;
import com.omgservers.module.user.impl.service.playerService.impl.method.syncPlayer.SyncPlayerMethod;
import com.omgservers.operation.handleInternalRequest.HandleInternalRequestOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PlayerServiceImpl implements PlayerService {

    final DeletePlayerMethod deletePlayerMethod;
    final SyncPlayerMethod syncPlayerMethod;
    final FindPlayerMethod findPlayerMethod;
    final GetPlayerMethod getPlayerMethod;

    final GetUserModuleClientOperation getUserModuleClientOperation;
    final HandleInternalRequestOperation handleInternalRequestOperation;

    @Override
    public Uni<GetPlayerResponse> getPlayer(GetPlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                GetPlayerRequest::validate,
                getUserModuleClientOperation::getClient,
                UserModuleClient::getPlayer,
                getPlayerMethod::getPlayer);
    }

    @Override
    public Uni<FindPlayerResponse> findPlayer(FindPlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                FindPlayerRequest::validate,
                getUserModuleClientOperation::getClient,
                UserModuleClient::findPlayer,
                findPlayerMethod::findPlayer);
    }

    @Override
    public Uni<SyncPlayerResponse> syncPlayer(SyncPlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                SyncPlayerRequest::validate,
                getUserModuleClientOperation::getClient,
                UserModuleClient::syncPlayer,
                syncPlayerMethod::syncPlayer);
    }

    @Override
    public Uni<DeletePlayerResponse> deletePlayer(DeletePlayerRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DeletePlayerRequest::validate,
                getUserModuleClientOperation::getClient,
                UserModuleClient::deletePlayer,
                deletePlayerMethod::deletePlayer);
    }
}
