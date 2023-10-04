package com.omgservers.module.user.impl.operation.selectPlayerAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideConflictException;
import com.omgservers.model.player.PlayerAttributesModel;
import com.omgservers.module.user.impl.mapper.PlayerModelMapper;
import com.omgservers.operation.executeSelectObject.ExecuteSelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectPlayerAttributesOperationImpl implements SelectPlayerAttributesOperation {

    final ExecuteSelectObjectOperation executeSelectObjectOperation;

    final PlayerModelMapper playerModelMapper;
    final ObjectMapper objectMapper;

    @Override
    public Uni<PlayerAttributesModel> selectPlayerAttributes(final SqlConnection sqlConnection,
                                                             final int shard,
                                                             final Long userId,
                                                             final Long playerId) {
        return executeSelectObjectOperation.executeSelectObject(
                sqlConnection,
                shard,
                """
                        select attributes
                        from $schema.tab_user_player
                        where user_id = $1 and id = $2
                        limit 1
                        """,
                Arrays.asList(userId, playerId),
                "Player attributes",
                row -> {
                    try {
                        return objectMapper.readValue(row.getString("attributes"), PlayerAttributesModel.class);
                    } catch (IOException e) {
                        throw new ServerSideConflictException(String.format("player attributes can't be parsed, " +
                                "userId=%d, playerId=%d", userId, playerId));
                    }
                });
    }
}
