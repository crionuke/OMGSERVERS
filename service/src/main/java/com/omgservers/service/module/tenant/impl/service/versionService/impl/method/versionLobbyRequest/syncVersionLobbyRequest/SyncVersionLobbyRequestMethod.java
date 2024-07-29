package com.omgservers.service.module.tenant.impl.service.versionService.impl.method.versionLobbyRequest.syncVersionLobbyRequest;

import com.omgservers.schema.module.tenant.SyncVersionLobbyRequestRequest;
import com.omgservers.schema.module.tenant.SyncVersionLobbyRequestResponse;
import io.smallrye.mutiny.Uni;

public interface SyncVersionLobbyRequestMethod {
    Uni<SyncVersionLobbyRequestResponse> syncVersionLobbyRequest(SyncVersionLobbyRequestRequest request);
}
