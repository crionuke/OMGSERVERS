package com.omgservers.module.gateway.impl.service.gatewayService;

import com.omgservers.model.dto.gateway.AssignClientRequest;
import com.omgservers.model.dto.gateway.AssignClientResponse;
import com.omgservers.model.dto.gateway.AssignRuntimeRequest;
import com.omgservers.model.dto.gateway.AssignRuntimeResponse;
import com.omgservers.model.dto.gateway.RespondMessageRequest;
import com.omgservers.model.dto.gateway.RespondMessageResponse;
import com.omgservers.model.dto.gateway.RevokeRuntimeRequest;
import com.omgservers.model.dto.gateway.RevokeRuntimeResponse;
import io.smallrye.mutiny.Uni;

public interface GatewayService {

    Uni<RespondMessageResponse> respondMessage(RespondMessageRequest request);

    Uni<AssignClientResponse> assignClient(AssignClientRequest request);

    Uni<AssignRuntimeResponse> assignRuntime(AssignRuntimeRequest request);

    Uni<RevokeRuntimeResponse> revokeRuntime(RevokeRuntimeRequest request);
}
