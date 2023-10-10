package com.omgservers.module.gateway.impl.service.gatewayService.impl.method.assignRuntime;

import com.omgservers.dto.gateway.AssignRuntimeRequest;
import com.omgservers.dto.gateway.AssignRuntimeResponse;
import io.smallrye.mutiny.Uni;

public interface AssignRuntimeMethod {
    Uni<AssignRuntimeResponse> assignRuntime(AssignRuntimeRequest request);
}
