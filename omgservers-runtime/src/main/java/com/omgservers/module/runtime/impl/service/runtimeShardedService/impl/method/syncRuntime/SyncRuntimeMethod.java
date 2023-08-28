package com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncRuntime;

import com.omgservers.dto.runtime.SyncRuntimeShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeShardedResponse;
import io.smallrye.mutiny.Uni;

public interface SyncRuntimeMethod {
    Uni<SyncRuntimeShardedResponse> syncRuntime(SyncRuntimeShardedRequest request);
}
