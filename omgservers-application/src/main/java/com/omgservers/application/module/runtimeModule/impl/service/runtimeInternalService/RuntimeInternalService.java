package com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService;

import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.request.SyncRuntimeInternalRequest;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.request.DeleteRuntimeInternalRequest;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.request.GetRuntimeInternalRequest;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.response.DeleteRuntimeInternalResponse;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.response.GetRuntimeInternalResponse;
import io.smallrye.mutiny.Uni;

public interface RuntimeInternalService {

    Uni<GetRuntimeInternalResponse> getRuntime(GetRuntimeInternalRequest request);

    Uni<Void> syncRuntime(SyncRuntimeInternalRequest request);

    Uni<DeleteRuntimeInternalResponse> deleteRuntime(DeleteRuntimeInternalRequest request);
}
