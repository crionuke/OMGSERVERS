package com.omgservers.module.runtime.impl.service.doService.impl.method.doSetObject;

import com.omgservers.dto.runtime.DoSetObjectRequest;
import com.omgservers.dto.runtime.DoSetObjectResponse;
import io.smallrye.mutiny.Uni;

public interface DoSetObjectMethod {
    Uni<DoSetObjectResponse> doSetObject(DoSetObjectRequest request);
}
