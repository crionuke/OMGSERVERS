package com.omgservers.application.module.userModule.impl.service.objectInternalService.impl.method.getObjectMethod;

import com.omgservers.dto.userModule.GetObjectInternalRequest;
import com.omgservers.dto.userModule.GetObjectInternalResponse;
import io.smallrye.mutiny.Uni;

public interface GetObjectMethod {
    Uni<GetObjectInternalResponse> getObject(GetObjectInternalRequest request);
}
