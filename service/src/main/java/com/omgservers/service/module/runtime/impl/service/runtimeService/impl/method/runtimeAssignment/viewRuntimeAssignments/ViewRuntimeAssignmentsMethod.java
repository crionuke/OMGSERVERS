package com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.runtimeAssignment.viewRuntimeAssignments;

import com.omgservers.schema.module.runtime.ViewRuntimeAssignmentsRequest;
import com.omgservers.schema.module.runtime.ViewRuntimeAssignmentsResponse;
import io.smallrye.mutiny.Uni;

public interface ViewRuntimeAssignmentsMethod {
    Uni<ViewRuntimeAssignmentsResponse> viewRuntimeAssignments(ViewRuntimeAssignmentsRequest request);
}
