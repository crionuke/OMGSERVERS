package com.omgservers.module.matchmaker.impl.service.matchmakerService.impl.method.syncMatchCommand;

import com.omgservers.dto.matchmaker.SyncMatchCommandRequest;
import com.omgservers.dto.matchmaker.SyncMatchCommandResponse;
import io.smallrye.mutiny.Uni;

public interface SyncMatchCommandMethod {
    Uni<SyncMatchCommandResponse> syncMatchCommand(SyncMatchCommandRequest request);
}
