package com.omgservers.service.factory.pool;

import com.omgservers.model.poolServer.PoolServerConfigModel;
import com.omgservers.model.poolServer.PoolServerModel;
import com.omgservers.model.poolServer.PoolServerQualifierEnum;
import com.omgservers.service.operation.generateId.GenerateIdOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class PoolServerModelFactory {

    final GenerateIdOperation generateIdOperation;

    public PoolServerModel create(final Long poolId,
                                  final PoolServerQualifierEnum qualifier,
                                  final PoolServerConfigModel config) {
        final var id = generateIdOperation.generateId();
        final var idempotencyKey = generateIdOperation.generateStringId();
        return create(id, poolId, qualifier, config, idempotencyKey);
    }

    public PoolServerModel create(final Long poolId,
                                  final PoolServerQualifierEnum qualifier,
                                  final PoolServerConfigModel config,
                                  final String idempotencyKey) {
        final var id = generateIdOperation.generateId();
        return create(id, poolId, qualifier, config, idempotencyKey);
    }

    public PoolServerModel create(final Long id,
                                  final Long poolId,
                                  final PoolServerQualifierEnum qualifier,
                                  final PoolServerConfigModel config,
                                  final String idempotencyKey) {
        final var now = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        final var poolServer = new PoolServerModel();
        poolServer.setId(id);
        poolServer.setIdempotencyKey(idempotencyKey);
        poolServer.setPoolId(poolId);
        poolServer.setCreated(now);
        poolServer.setModified(now);
        poolServer.setQualifier(qualifier);
        poolServer.setConfig(config);
        poolServer.setDeleted(Boolean.FALSE);

        return poolServer;
    }
}
