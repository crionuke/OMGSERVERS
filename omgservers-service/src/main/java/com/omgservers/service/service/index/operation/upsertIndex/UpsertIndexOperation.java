package com.omgservers.service.service.index.operation.upsertIndex;

import com.omgservers.schema.model.index.IndexModel;
import com.omgservers.service.operation.server.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface UpsertIndexOperation {
    Uni<Boolean> upsertIndex(ChangeContext<?> changeContext,
                             SqlConnection sqlConnection,
                             IndexModel index);
}
