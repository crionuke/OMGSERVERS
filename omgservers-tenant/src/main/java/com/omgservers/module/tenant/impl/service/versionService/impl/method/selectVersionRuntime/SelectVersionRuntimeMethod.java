package com.omgservers.module.tenant.impl.service.versionService.impl.method.selectVersionRuntime;

import com.omgservers.dto.tenant.SelectVersionRuntimeRequest;
import com.omgservers.dto.tenant.SelectVersionRuntimeResponse;
import io.smallrye.mutiny.Uni;

public interface SelectVersionRuntimeMethod {
    Uni<SelectVersionRuntimeResponse> selectVersionRuntime(SelectVersionRuntimeRequest request);
}
