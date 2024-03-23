package com.omgservers.service.module.user.impl.operation.selectPlayerByUserIdAndStageId;

import com.omgservers.model.player.PlayerModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface SelectPlayerByUserIdAndStageIdOperation {
    Uni<PlayerModel> selectPlayerByUserIdAndStageId(SqlConnection sqlConnection,
                                                    int shard,
                                                    Long userId,
                                                    Long stageId);
}
