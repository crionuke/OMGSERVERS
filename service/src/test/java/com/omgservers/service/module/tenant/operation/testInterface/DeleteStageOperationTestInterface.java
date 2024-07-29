package com.omgservers.service.module.tenant.operation.testInterface;

import com.omgservers.service.module.tenant.impl.operation.stage.deleteStage.DeleteStageOperation;
import com.omgservers.service.server.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DeleteStageOperationTestInterface {
    private static final long TIMEOUT = 1L;

    final DeleteStageOperation deleteStageOperation;

    final PgPool pgPool;

    public ChangeContext<Boolean> deleteStage(final int shard,
                                              final Long tenantId,
                                              final Long id) {
        return Uni.createFrom().context(context -> {
                    final var changeContext = new ChangeContext<Boolean>(context);
                    return pgPool.withTransaction(sqlConnection -> deleteStageOperation
                                    .deleteStage(changeContext, sqlConnection, shard, tenantId, id))
                            .invoke(changeContext::setResult)
                            .replaceWith(changeContext);
                })
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
