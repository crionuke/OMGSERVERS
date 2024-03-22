package com.omgservers.service.module.client.impl.operation.selectClientMatchmakerRefByClientIdAndRuntimeId;

import com.omgservers.model.clientMatchmakerRef.ClientMatchmakerRefModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface SelectClientMatchmakerRefByClientIdAndMatchmakerIdOperation {
    Uni<ClientMatchmakerRefModel> selectClientMatchmakerRefByClientIdAndMatchmakerId(SqlConnection sqlConnection,
                                                                                     int shard,
                                                                                     Long clientId,
                                                                                     Long matchmakerId);
}
