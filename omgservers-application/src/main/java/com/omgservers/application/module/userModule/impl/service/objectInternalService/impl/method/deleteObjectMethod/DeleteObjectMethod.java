package com.omgservers.application.module.userModule.impl.service.objectInternalService.impl.method.deleteObjectMethod;

import com.omgservers.dto.userModule.DeleteObjectInternalRequest;
import com.omgservers.dto.userModule.DeleteObjectInternalResponse;
import io.smallrye.mutiny.Uni;

public interface DeleteObjectMethod {
    Uni<DeleteObjectInternalResponse> deleteObject(DeleteObjectInternalRequest request);
}
