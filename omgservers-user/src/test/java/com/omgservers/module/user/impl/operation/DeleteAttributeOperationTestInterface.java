package com.omgservers.module.user.impl.operation;

import com.omgservers.module.user.impl.operation.deleteAttribute.DeleteAttributeOperation;
import com.omgservers.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DeleteAttributeOperationTestInterface {
    private static final long TIMEOUT = 1L;

    final DeleteAttributeOperation deleteAttributeOperation;

    final PgPool pgPool;

    public ChangeContext<Boolean> deleteAttribute(final int shard,
                                                  final Long userId,
                                                  final Long playerId,
                                                  final String name) {
        return Uni.createFrom().context(context -> {
                    final var changeContext = new ChangeContext<Boolean>(context);
                    return pgPool.withTransaction(sqlConnection -> deleteAttributeOperation
                                    .deleteAttribute(changeContext, sqlConnection, shard, userId, playerId, name))
                            .invoke(changeContext::setResult)
                            .replaceWith(changeContext);
                })
                .invoke(changeContext -> log.info("Change context, {}", changeContext))
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
