package com.omgservers.service.module.user.operation.testInterface;

import com.omgservers.model.user.UserModel;
import com.omgservers.service.module.user.impl.operation.selectUser.SelectUserOperation;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class SelectUserOperationTestInterface {
    private static final long TIMEOUT = 1L;

    final SelectUserOperation selectUserOperation;

    final PgPool pgPool;

    public UserModel selectUser(final int shard,
                                final Long id,
                                final Boolean deleted) {
        return pgPool.withTransaction(sqlConnection -> selectUserOperation
                        .selectUser(sqlConnection, shard, id, deleted))
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
