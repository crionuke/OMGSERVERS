package com.omgservers.service.module.tenant.impl.operation.deleteVersionLobbyRef;

import com.omgservers.service.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface DeleteVersionLobbyRefOperation {
    Uni<Boolean> deleteVersionLobbyRef(ChangeContext<?> changeContext,
                                       SqlConnection sqlConnection,
                                       int shard,
                                       Long tenantId,
                                       Long id);
}
