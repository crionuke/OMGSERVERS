package com.omgservers.service.shard.tenant.impl.operation.tenantBuildRequest;

import com.omgservers.schema.model.tenantBuildRequest.TenantBuildRequestModel;
import com.omgservers.service.operation.server.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface UpsertTenantBuildRequestOperation {
    Uni<Boolean> execute(ChangeContext<?> changeContext,
                         SqlConnection sqlConnection,
                         int shard,
                         TenantBuildRequestModel tenantBuildRequest);
}
