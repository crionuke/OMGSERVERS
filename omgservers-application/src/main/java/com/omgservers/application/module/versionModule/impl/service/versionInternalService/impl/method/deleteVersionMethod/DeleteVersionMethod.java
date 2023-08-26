package com.omgservers.application.module.versionModule.impl.service.versionInternalService.impl.method.deleteVersionMethod;

import com.omgservers.dto.versionModule.DeleteVersionRoutedRequest;
import com.omgservers.dto.versionModule.DeleteVersionInternalResponse;
import io.smallrye.mutiny.Uni;

public interface DeleteVersionMethod {
    Uni<DeleteVersionInternalResponse> deleteVersion(DeleteVersionRoutedRequest request);
}
