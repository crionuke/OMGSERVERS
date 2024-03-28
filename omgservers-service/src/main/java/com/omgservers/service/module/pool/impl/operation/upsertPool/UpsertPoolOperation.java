package com.omgservers.service.module.pool.impl.operation.upsertPool;

import com.omgservers.model.pool.PoolModel;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface UpsertPoolOperation {
    Uni<Boolean> upsertPool(ChangeContext<?> changeContext,
                            SqlConnection sqlConnection,
                            int shard,
                            PoolModel pool);
}
