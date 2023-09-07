package com.omgservers.module.matchmaker.impl.operation.selectRequest;

import com.omgservers.model.request.RequestModel;
import com.omgservers.module.matchmaker.impl.mappers.RequestModelMapper;
import com.omgservers.operation.executeSelectObject.ExecuteSelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectRequestOperationImpl implements SelectRequestOperation {

    final ExecuteSelectObjectOperation executeSelectObjectOperation;

    final RequestModelMapper requestModelMapper;

    @Override
    public Uni<RequestModel> selectRequest(final SqlConnection sqlConnection,
                                           final int shard,
                                           final Long matchmakerId,
                                           final Long id) {
        return executeSelectObjectOperation.executeSelectObject(
                sqlConnection,
                shard,
                """
                        select id, matchmaker_id, created, modified, user_id, client_id, mode, config
                        from $schema.tab_matchmaker_request
                        where matchmaker_id = $1 and id = $2
                        limit 1
                        """,
                Arrays.asList(matchmakerId, id),
                "Request",
                requestModelMapper::fromRow);
    }
}
