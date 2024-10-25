package com.omgservers.service.module.matchmaker.operation.testInterface;

import com.omgservers.service.module.matchmaker.impl.operation.matchmakerRequest.DeleteMatchmakerRequestOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DeleteMatchmakerRequestOperationTestInterface {
    private static final long TIMEOUT = 1L;

    final DeleteMatchmakerRequestOperation deleteMatchmakerRequestOperation;

    final PgPool pgPool;

    public ChangeContext<Boolean> deleteMatchmakerRequest(final int shard,
                                                          final Long matchmakerId,
                                                          final Long id) {
        return Uni.createFrom().context(context -> {
                    final var changeContext = new ChangeContext<Boolean>(context);
                    return pgPool.withTransaction(sqlConnection -> deleteMatchmakerRequestOperation
                                    .execute(changeContext, sqlConnection, shard, matchmakerId, id))
                            .invoke(changeContext::setResult)
                            .replaceWith(changeContext);
                })
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
