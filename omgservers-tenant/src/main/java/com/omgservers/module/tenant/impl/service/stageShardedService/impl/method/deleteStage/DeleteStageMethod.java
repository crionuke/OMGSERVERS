package com.omgservers.module.tenant.impl.service.stageShardedService.impl.method.deleteStage;

import com.omgservers.dto.tenant.DeleteStageShardedRequest;
import com.omgservers.dto.tenant.DeleteStageInternalResponse;
import io.smallrye.mutiny.Uni;

public interface DeleteStageMethod {
    Uni<DeleteStageInternalResponse> deleteStage(DeleteStageShardedRequest request);
}
