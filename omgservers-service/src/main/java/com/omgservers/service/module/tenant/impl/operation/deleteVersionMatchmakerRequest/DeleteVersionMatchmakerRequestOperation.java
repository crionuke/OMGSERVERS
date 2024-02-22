package com.omgservers.service.module.tenant.impl.operation.deleteVersionMatchmakerRequest;

import com.omgservers.service.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface DeleteVersionMatchmakerRequestOperation {
    Uni<Boolean> deleteVersionMatchmakerRequest(ChangeContext<?> changeContext,
                                                SqlConnection sqlConnection,
                                                int shard,
                                                Long tenantId,
                                                Long id);
}
