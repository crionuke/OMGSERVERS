package com.omgservers.service.module.alias.impl.operation.alias;

import com.omgservers.schema.model.alias.AliasModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface SelectAliasOperation {
    Uni<AliasModel> execute(SqlConnection sqlConnection,
                            int shard,
                            Long id);
}
