package com.omgservers.module.internal.impl.service.changeService.impl.method.changeWithLog;

import com.omgservers.dto.internal.ChangeWithLogRequest;
import com.omgservers.dto.internal.ChangeWithLogResponse;
import io.smallrye.mutiny.Uni;

public interface ChangeWithLogMethod {

    Uni<ChangeWithLogResponse> changeWithLog(ChangeWithLogRequest request);
}
