package com.omgservers.service.shard.lobby.impl.operation.lobby.selectLobby;

import com.omgservers.schema.model.lobby.LobbyModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface SelectLobbyOperation {
    Uni<LobbyModel> selectLobby(SqlConnection sqlConnection,
                                int shard,
                                Long id);
}
