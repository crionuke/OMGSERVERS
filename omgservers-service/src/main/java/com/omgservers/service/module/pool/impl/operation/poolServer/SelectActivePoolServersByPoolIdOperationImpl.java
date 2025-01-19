package com.omgservers.service.module.pool.impl.operation.poolServer;

import com.omgservers.schema.model.poolServer.PoolServerModel;
import com.omgservers.service.module.pool.impl.mappers.PoolServerModelMapper;
import com.omgservers.service.operation.server.SelectListOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectActivePoolServersByPoolIdOperationImpl
        implements SelectActivePoolServersByPoolIdOperation {

    final SelectListOperation selectListOperation;

    final PoolServerModelMapper poolServerModelMapper;

    @Override
    public Uni<List<PoolServerModel>> execute(
            final SqlConnection sqlConnection,
            final int shard,
            final Long poolId) {
        return selectListOperation.selectList(
                sqlConnection,
                shard,
                """
                        select
                            id, idempotency_key, pool_id, created, modified, qualifier, config, deleted
                        from $schema.tab_pool_server
                        where pool_id = $1 and deleted = false
                        order by id asc
                        """,
                List.of(poolId),
                "Pool server",
                poolServerModelMapper::execute);
    }
}
