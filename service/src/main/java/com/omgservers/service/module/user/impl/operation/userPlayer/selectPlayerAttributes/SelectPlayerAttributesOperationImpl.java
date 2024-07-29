package com.omgservers.service.module.user.impl.operation.userPlayer.selectPlayerAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.schema.model.player.PlayerAttributesModel;
import com.omgservers.schema.model.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideConflictException;
import com.omgservers.service.module.user.impl.mapper.PlayerModelMapper;
import com.omgservers.service.operation.selectObject.SelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectPlayerAttributesOperationImpl implements SelectPlayerAttributesOperation {

    final SelectObjectOperation selectObjectOperation;

    final PlayerModelMapper playerModelMapper;
    final ObjectMapper objectMapper;

    @Override
    public Uni<PlayerAttributesModel> selectPlayerAttributes(final SqlConnection sqlConnection,
                                                             final int shard,
                                                             final Long userId,
                                                             final Long playerId) {
        return selectObjectOperation.selectObject(
                sqlConnection,
                shard,
                """
                        select attributes
                        from $schema.tab_user_player
                        where user_id = $1 and id = $2
                        limit 1
                        """,
                List.of(userId, playerId),
                "Player attributes",
                row -> {
                    try {
                        return objectMapper.readValue(row.getString("attributes"), PlayerAttributesModel.class);
                    } catch (IOException e) {
                        throw new ServerSideConflictException(ExceptionQualifierEnum.DB_DATA_CORRUPTED,
                                String.format("player attributes can't be parsed, userId=%d, playerId=%d",
                                        userId, playerId));
                    }
                });
    }
}
