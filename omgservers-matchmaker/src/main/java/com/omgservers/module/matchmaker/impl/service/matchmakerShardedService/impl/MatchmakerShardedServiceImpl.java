package com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl;

import com.omgservers.dto.matchmaker.DeleteMatchShardedResponse;
import com.omgservers.dto.matchmaker.DeleteMatchShardedRequest;
import com.omgservers.dto.matchmaker.DeleteMatchmakerShardedResponse;
import com.omgservers.dto.matchmaker.DeleteMatchmakerShardedRequest;
import com.omgservers.dto.matchmaker.DeleteRequestShardedResponse;
import com.omgservers.dto.matchmaker.DeleteRequestShardedRequest;
import com.omgservers.dto.matchmaker.DoMatchmakingShardedResponse;
import com.omgservers.dto.matchmaker.DoMatchmakingShardedRequest;
import com.omgservers.dto.matchmaker.GetMatchShardedResponse;
import com.omgservers.dto.matchmaker.GetMatchShardedRequest;
import com.omgservers.dto.matchmaker.GetMatchmakerShardedResponse;
import com.omgservers.dto.matchmaker.GetMatchmakerShardedRequest;
import com.omgservers.dto.matchmaker.SyncMatchShardedResponse;
import com.omgservers.dto.matchmaker.SyncMatchShardedRequest;
import com.omgservers.dto.matchmaker.SyncMatchmakerShardedResponse;
import com.omgservers.dto.matchmaker.SyncMatchmakerShardedRequest;
import com.omgservers.dto.matchmaker.SyncRequestShardedResponse;
import com.omgservers.dto.matchmaker.SyncRequestShardedRequest;
import com.omgservers.module.matchmaker.impl.operation.getMatchmakerModuleClient.GetMatchmakerModuleClientOperation;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.MatchmakerShardedService;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.deleteMatch.DeleteMatchMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.deleteMatchmaker.DeleteMatchmakerMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.deleteRequest.DeleteRequestMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.doMatchmaking.DoMatchmakingMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.getMatch.GetMatchMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.getMatchmaker.GetMatchmakerMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.syncMatch.SyncMatchMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.syncMatchmaker.SyncMatchmakerMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerShardedService.impl.method.syncRequest.SyncRequestMethod;
import com.omgservers.module.matchmaker.impl.service.matchmakerWebService.impl.serviceApi.MatchmakerServiceApi;
import com.omgservers.operation.calculateShard.CalculateShardOperation;
import com.omgservers.operation.handleInternalRequest.HandleInternalRequestOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class MatchmakerShardedServiceImpl implements MatchmakerShardedService {

    final DeleteMatchmakerMethod deleteMatchmakerMethod;
    final SyncMatchmakerMethod syncMatchmakerMethod;
    final GetMatchmakerMethod getMatchmakerMethod;
    final SyncRequestMethod syncRequestMethod;
    final DeleteRequestMethod deleteRequestMethod;
    final DoMatchmakingMethod doMatchmakingMethod;
    final DeleteMatchMethod deleteMatchMethod;
    final SyncMatchMethod syncMatchMethod;
    final GetMatchMethod getMatchMethod;

    final GetMatchmakerModuleClientOperation getMatchServiceApiClientOperation;
    final HandleInternalRequestOperation handleInternalRequestOperation;
    final CalculateShardOperation calculateShardOperation;

    @Override
    public Uni<SyncMatchmakerShardedResponse> syncMatchmaker(SyncMatchmakerShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                SyncMatchmakerShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::syncMatchmaker,
                syncMatchmakerMethod::syncMatchmaker);
    }

    @Override
    public Uni<GetMatchmakerShardedResponse> getMatchmaker(GetMatchmakerShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                GetMatchmakerShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::getMatchmaker,
                getMatchmakerMethod::getMatchmaker);
    }

    @Override
    public Uni<DeleteMatchmakerShardedResponse> deleteMatchmaker(DeleteMatchmakerShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DeleteMatchmakerShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::deleteMatchmaker,
                deleteMatchmakerMethod::deleteMatchmaker);
    }

    @Override
    public Uni<SyncRequestShardedResponse> syncRequest(SyncRequestShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                SyncRequestShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::syncRequest,
                syncRequestMethod::syncRequest);
    }

    @Override
    public Uni<DeleteRequestShardedResponse> deleteRequest(DeleteRequestShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DeleteRequestShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::deleteRequest,
                deleteRequestMethod::deleteRequest);
    }

    @Override
    public Uni<GetMatchShardedResponse> getMatch(GetMatchShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                GetMatchShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::getMatch,
                getMatchMethod::getMatch);
    }

    @Override
    public Uni<SyncMatchShardedResponse> syncMatch(SyncMatchShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                SyncMatchShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::syncMatch,
                syncMatchMethod::syncMatch);
    }

    @Override
    public Uni<DeleteMatchShardedResponse> deleteMatch(DeleteMatchShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DeleteMatchShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::deleteMatch,
                deleteMatchMethod::deleteMatch);
    }

    @Override
    public Uni<DoMatchmakingShardedResponse> doMatchmaking(DoMatchmakingShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DoMatchmakingShardedRequest::validate,
                getMatchServiceApiClientOperation::getClient,
                MatchmakerServiceApi::doMatchmaking,
                doMatchmakingMethod::doMatchmaking);
    }
}
