package com.omgservers.module.gateway.impl.service.connectionService;

import com.omgservers.module.gateway.impl.service.connectionService.request.AssignClientRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.AssignRuntimeRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.CreateConnectionRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.DeleteConnectionRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetAssignedClientRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetAssignedRuntimeRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetConnectionRequest;
import com.omgservers.module.gateway.impl.service.connectionService.request.GetSessionRequest;
import com.omgservers.module.gateway.impl.service.connectionService.response.DeleteConnectionResponse;
import com.omgservers.module.gateway.impl.service.connectionService.response.GetAssignedClientResponse;
import com.omgservers.module.gateway.impl.service.connectionService.response.GetAssignedRuntimeResponse;
import com.omgservers.module.gateway.impl.service.connectionService.response.GetConnectionResponse;
import com.omgservers.module.gateway.impl.service.connectionService.response.GetSessionResponse;
import jakarta.validation.Valid;

public interface ConnectionService {

    void createConnection(@Valid CreateConnectionRequest request);

    DeleteConnectionResponse deleteConnection(@Valid DeleteConnectionRequest request);

    void assignClient(@Valid AssignClientRequest request);

    void assignRuntime(@Valid AssignRuntimeRequest request);

    GetConnectionResponse getConnection(@Valid GetConnectionRequest request);

    GetSessionResponse getSession(@Valid GetSessionRequest request);

    GetAssignedClientResponse getAssignedClient(@Valid GetAssignedClientRequest request);

    GetAssignedRuntimeResponse getAssignedRuntime(@Valid GetAssignedRuntimeRequest request);
}
