package com.omgservers.module.tenant.impl.service.versionService.impl.method.deleteVersionMatchmaker;

import com.omgservers.dto.tenant.DeleteVersionMatchmakerRequest;
import com.omgservers.dto.tenant.DeleteVersionMatchmakerResponse;
import io.smallrye.mutiny.Uni;

public interface DeleteVersionMatchmakerMethod {
    Uni<DeleteVersionMatchmakerResponse> deleteVersionMatchmaker(DeleteVersionMatchmakerRequest request);
}
