package com.omgservers.service.entrypoint.player.impl.service.playerService.impl.method.createToken;

import com.omgservers.schema.entrypoint.player.CreateTokenPlayerRequest;
import com.omgservers.schema.entrypoint.player.CreateTokenPlayerResponse;
import io.smallrye.mutiny.Uni;

public interface CreateTokenMethod {
    Uni<CreateTokenPlayerResponse> createToken(CreateTokenPlayerRequest request);
}
