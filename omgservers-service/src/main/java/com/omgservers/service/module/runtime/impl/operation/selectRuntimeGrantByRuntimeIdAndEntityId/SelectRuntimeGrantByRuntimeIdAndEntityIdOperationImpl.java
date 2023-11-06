package com.omgservers.service.module.runtime.impl.operation.selectRuntimeGrantByRuntimeIdAndEntityId;

import com.omgservers.model.runtimeGrant.RuntimeGrantModel;
import com.omgservers.service.module.runtime.impl.mapper.RuntimeGrantModelMapper;
import com.omgservers.service.operation.selectObject.SelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectRuntimeGrantByRuntimeIdAndEntityIdOperationImpl implements
        SelectRuntimeGrantByRuntimeIdAndEntityIdOperation {

    final SelectObjectOperation selectObjectOperation;

    final RuntimeGrantModelMapper runtimeGrantModelMapper;

    @Override
    public Uni<RuntimeGrantModel> selectRuntimeGrantByRuntimeIdAndEntityId(final SqlConnection sqlConnection,
                                                                           final int shard,
                                                                           final Long runtimeId,
                                                                           final Long entityId) {
        return selectObjectOperation.selectObject(
                sqlConnection,
                shard,
                """
                        select id, runtime_id, created, modified, shard_key, entity_id, type, deleted
                        from $schema.tab_runtime_grant
                        where runtime_id = $1 and entity_id = $2 and deleted = false
                        limit 1
                        """,
                Arrays.asList(runtimeId, entityId),
                "Runtime grant",
                runtimeGrantModelMapper::fromRow);
    }
}
