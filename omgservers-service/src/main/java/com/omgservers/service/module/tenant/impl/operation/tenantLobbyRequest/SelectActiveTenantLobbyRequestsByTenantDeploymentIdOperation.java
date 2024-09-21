package com.omgservers.service.module.tenant.impl.operation.tenantLobbyRequest;

import com.omgservers.schema.model.tenantLobbyRequest.TenantLobbyRequestModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.util.List;

public interface SelectActiveTenantLobbyRequestsByTenantDeploymentIdOperation {
    Uni<List<TenantLobbyRequestModel>> execute(SqlConnection sqlConnection,
                                               int shard,
                                               Long tenantId,
                                               Long tenantDeploymentId);
}
