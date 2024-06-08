package com.omgservers.service.module.root.impl.operation.rootEntityRef.selectRootEntityRefByRootIdAndEntityId;

import com.omgservers.model.rootEntityRef.RootEntityRefModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface SelectRootEntityRefByRootIdAndEntityIdOperation {
    Uni<RootEntityRefModel> selectRootEntityRefByRootIdAndEntityId(
            SqlConnection sqlConnection,
            int shard,
            Long rootId,
            Long entityId);
}
