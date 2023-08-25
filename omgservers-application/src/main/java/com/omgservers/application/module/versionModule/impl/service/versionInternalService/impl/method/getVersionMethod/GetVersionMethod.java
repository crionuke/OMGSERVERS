package com.omgservers.application.module.versionModule.impl.service.versionInternalService.impl.method.getVersionMethod;

import com.omgservers.dto.versionModule.GetVersionInternalRequest;
import com.omgservers.dto.versionModule.GetVersionInternalResponse;
import io.smallrye.mutiny.Uni;

public interface GetVersionMethod {

    Uni<GetVersionInternalResponse> getVersion(GetVersionInternalRequest request);
}
