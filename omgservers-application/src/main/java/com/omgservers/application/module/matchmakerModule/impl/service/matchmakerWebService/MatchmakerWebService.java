package com.omgservers.application.module.matchmakerModule.impl.service.matchmakerWebService;

import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.request.*;
import com.omgservers.application.module.matchmakerModule.impl.service.matchmakerInternalService.response.*;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

public interface MatchmakerWebService {

    Uni<Void> createMatchmaker(CreateMatchmakerInternalRequest request);

    Uni<GetMatchmakerInternalResponse> getMatchmaker(GetMatchmakerInternalRequest request);

    Uni<DeleteMatchmakerInternalResponse> deleteMatchmaker(DeleteMatchmakerInternalRequest request);

    Uni<CreateRequestInternalResponse> createRequest(CreateRequestInternalRequest request);

    Uni<DeleteRequestInternalResponse> deleteRequest(DeleteRequestInternalRequest request);

    Uni<GetMatchInternalResponse> getMatch(GetMatchInternalRequest request);

    Uni<SyncMatchInternalResponse> syncMatch(SyncMatchInternalRequest request);

    Uni<DeleteMatchInternalResponse> deleteMatch(DeleteMatchInternalRequest request);

    Uni<DoMatchmakingInternalResponse> doMatchmaking(DoMatchmakingInternalRequest request);
}
