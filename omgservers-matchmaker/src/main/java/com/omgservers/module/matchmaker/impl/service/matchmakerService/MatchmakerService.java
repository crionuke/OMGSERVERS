package com.omgservers.module.matchmaker.impl.service.matchmakerService;

import com.omgservers.dto.matchmaker.DeleteMatchClientRequest;
import com.omgservers.dto.matchmaker.DeleteMatchClientResponse;
import com.omgservers.dto.matchmaker.DeleteMatchCommandRequest;
import com.omgservers.dto.matchmaker.DeleteMatchCommandResponse;
import com.omgservers.dto.matchmaker.DeleteMatchRequest;
import com.omgservers.dto.matchmaker.DeleteMatchResponse;
import com.omgservers.dto.matchmaker.DeleteMatchmakerCommandRequest;
import com.omgservers.dto.matchmaker.DeleteMatchmakerCommandResponse;
import com.omgservers.dto.matchmaker.DeleteMatchmakerRequest;
import com.omgservers.dto.matchmaker.DeleteMatchmakerResponse;
import com.omgservers.dto.matchmaker.DeleteRequestRequest;
import com.omgservers.dto.matchmaker.DeleteRequestResponse;
import com.omgservers.dto.matchmaker.FindMatchClientRequest;
import com.omgservers.dto.matchmaker.FindMatchClientResponse;
import com.omgservers.dto.matchmaker.GetMatchClientRequest;
import com.omgservers.dto.matchmaker.GetMatchClientResponse;
import com.omgservers.dto.matchmaker.GetMatchRequest;
import com.omgservers.dto.matchmaker.GetMatchResponse;
import com.omgservers.dto.matchmaker.GetMatchmakerRequest;
import com.omgservers.dto.matchmaker.GetMatchmakerResponse;
import com.omgservers.dto.matchmaker.GetMatchmakerStateRequest;
import com.omgservers.dto.matchmaker.GetMatchmakerStateResponse;
import com.omgservers.dto.matchmaker.SyncMatchClientRequest;
import com.omgservers.dto.matchmaker.SyncMatchClientResponse;
import com.omgservers.dto.matchmaker.SyncMatchCommandRequest;
import com.omgservers.dto.matchmaker.SyncMatchCommandResponse;
import com.omgservers.dto.matchmaker.SyncMatchRequest;
import com.omgservers.dto.matchmaker.SyncMatchResponse;
import com.omgservers.dto.matchmaker.SyncMatchmakerCommandRequest;
import com.omgservers.dto.matchmaker.SyncMatchmakerCommandResponse;
import com.omgservers.dto.matchmaker.SyncMatchmakerRequest;
import com.omgservers.dto.matchmaker.SyncMatchmakerResponse;
import com.omgservers.dto.matchmaker.SyncRequestRequest;
import com.omgservers.dto.matchmaker.SyncRequestResponse;
import com.omgservers.dto.matchmaker.UpdateMatchmakerStateRequest;
import com.omgservers.dto.matchmaker.UpdateMatchmakerStateResponse;
import com.omgservers.dto.matchmaker.ViewMatchClientsRequest;
import com.omgservers.dto.matchmaker.ViewMatchClientsResponse;
import com.omgservers.dto.matchmaker.ViewMatchCommandsRequest;
import com.omgservers.dto.matchmaker.ViewMatchCommandsResponse;
import com.omgservers.dto.matchmaker.ViewMatchesRequest;
import com.omgservers.dto.matchmaker.ViewMatchesResponse;
import com.omgservers.dto.matchmaker.ViewMatchmakerCommandsRequest;
import com.omgservers.dto.matchmaker.ViewMatchmakerCommandsResponse;
import com.omgservers.dto.matchmaker.ViewRequestsRequest;
import com.omgservers.dto.matchmaker.ViewRequestsResponse;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;

public interface MatchmakerService {
    Uni<SyncMatchmakerResponse> syncMatchmaker(@Valid SyncMatchmakerRequest request);

    Uni<GetMatchmakerResponse> getMatchmaker(@Valid GetMatchmakerRequest request);

    Uni<DeleteMatchmakerResponse> deleteMatchmaker(@Valid DeleteMatchmakerRequest request);

    Uni<GetMatchmakerStateResponse> getMatchmakerState(@Valid GetMatchmakerStateRequest request);

    Uni<UpdateMatchmakerStateResponse> updateMatchmakerState(@Valid UpdateMatchmakerStateRequest request);

    Uni<SyncMatchmakerCommandResponse> syncMatchmakerCommand(@Valid SyncMatchmakerCommandRequest request);

    Uni<DeleteMatchmakerCommandResponse> deleteMatchmakerCommand(@Valid DeleteMatchmakerCommandRequest request);

    Uni<ViewMatchmakerCommandsResponse> viewMatchmakerCommands(@Valid ViewMatchmakerCommandsRequest request);

    Uni<SyncRequestResponse> syncRequest(@Valid SyncRequestRequest request);

    Uni<DeleteRequestResponse> deleteRequest(@Valid DeleteRequestRequest request);

    Uni<ViewRequestsResponse> viewRequests(@Valid ViewRequestsRequest request);

    Uni<GetMatchResponse> getMatch(@Valid GetMatchRequest request);

    Uni<SyncMatchResponse> syncMatch(@Valid SyncMatchRequest request);

    Uni<DeleteMatchResponse> deleteMatch(@Valid DeleteMatchRequest request);

    Uni<ViewMatchesResponse> viewMatches(@Valid ViewMatchesRequest request);

    Uni<SyncMatchCommandResponse> syncMatchCommand(@Valid SyncMatchCommandRequest request);

    Uni<DeleteMatchCommandResponse> deleteMatchCommand(@Valid DeleteMatchCommandRequest request);

    Uni<ViewMatchCommandsResponse> viewMatchCommands(@Valid ViewMatchCommandsRequest request);

    Uni<GetMatchClientResponse> getMatchClient(@Valid GetMatchClientRequest request);

    Uni<SyncMatchClientResponse> syncMatchClient(@Valid SyncMatchClientRequest request);

    Uni<DeleteMatchClientResponse> deleteMatchClient(@Valid DeleteMatchClientRequest request);

    Uni<FindMatchClientResponse> findMatchClient(@Valid FindMatchClientRequest request);

    Uni<ViewMatchClientsResponse> viewMatchClients(@Valid ViewMatchClientsRequest request);
}
