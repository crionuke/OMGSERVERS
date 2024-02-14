package com.omgservers.service.module.lobby.impl.operation.hasLobby;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface HasLobbyOperation {
    Uni<Boolean> hasLobby(SqlConnection sqlConnection,
                          int shard,
                          Long id);
}
