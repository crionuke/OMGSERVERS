package com.omgservers.service.shard.root.impl.operation.root.upsertRoot;

import com.omgservers.schema.model.root.RootModel;
import com.omgservers.service.operation.server.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface UpsertRootOperation {
    Uni<Boolean> upsertRoot(ChangeContext<?> changeContext,
                            SqlConnection sqlConnection,
                            int shard,
                            RootModel root);
}
