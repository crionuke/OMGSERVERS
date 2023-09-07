package com.omgservers.module.matchmaker.impl.service.matchmakerService.impl.method.getMatchClient;

import com.omgservers.dto.matchmaker.GetMatchClientRequest;
import com.omgservers.dto.matchmaker.GetMatchClientResponse;
import io.smallrye.mutiny.Uni;

import java.time.Duration;

public interface GetMatchClientMethod {
    Uni<GetMatchClientResponse> getMatchClient(GetMatchClientRequest request);

    default GetMatchClientResponse getMatchClient(long timeout, GetMatchClientRequest request) {
        return getMatchClient(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
