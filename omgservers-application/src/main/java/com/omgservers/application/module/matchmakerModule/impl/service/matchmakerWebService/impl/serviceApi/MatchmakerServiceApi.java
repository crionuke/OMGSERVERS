package com.omgservers.application.module.matchmakerModule.impl.service.matchmakerWebService.impl.serviceApi;

import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.request.*;
import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.response.*;
import io.smallrye.mutiny.Uni;

import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/omgservers/service-api/matchmaker-api/v1/request")
public interface MatchmakerServiceApi {

    @PUT
    @Path("/create-matchmaker")
    Uni<Void> createMatchmaker(CreateMatchmakerInternalRequest request);

    @PUT
    @Path("/get-matchmaker")
    Uni<GetMatchmakerInternalResponse> getMatchmaker(GetMatchmakerInternalRequest request);

    @PUT
    @Path("/delete-matchmaker")
    Uni<DeleteMatchmakerInternalResponse> deleteMatchmaker(DeleteMatchmakerInternalRequest request);

    @PUT
    @Path("/create-request")
    Uni<CreateRequestInternalResponse> createRequest(CreateRequestInternalRequest request);

    @PUT
    @Path("/delete-request")
    Uni<DeleteRequestInternalResponse> deleteRequest(DeleteRequestInternalRequest request);

    @PUT
    @Path("/get-match")
    Uni<GetMatchInternalResponse> getMatch(GetMatchInternalRequest request);

    @PUT
    @Path("/create-match")
    Uni<SyncMatchInternalResponse> syncMatch(SyncMatchInternalRequest request);

    @PUT
    @Path("/delete-match")
    Uni<DeleteMatchInternalResponse> deleteMatch(DeleteMatchInternalRequest request);

    @PUT
    @Path("/do-matchmaking")
    Uni<DoMatchmakingInternalResponse> doMatchmaking(DoMatchmakingInternalRequest request);
}
