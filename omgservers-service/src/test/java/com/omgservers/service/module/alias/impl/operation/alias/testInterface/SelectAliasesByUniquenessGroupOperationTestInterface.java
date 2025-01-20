package com.omgservers.service.module.alias.impl.operation.alias.testInterface;

import com.omgservers.schema.model.alias.AliasModel;
import com.omgservers.service.module.alias.impl.operation.alias.SelectAliasesByUniquenessGroupOperation;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SelectAliasesByUniquenessGroupOperationTestInterface {
    private static final long TIMEOUT = 1L;

    final SelectAliasesByUniquenessGroupOperation selectAliasesByUniquenessGroupOperation;

    final PgPool pgPool;

    public List<AliasModel> execute(final Long shardKey, final Long uniquenessGroup) {
        return pgPool.withTransaction(sqlConnection -> selectAliasesByUniquenessGroupOperation
                        .execute(sqlConnection, 0, shardKey, uniquenessGroup))
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
