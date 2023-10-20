package com.omgservers.module.tenant.impl.service.versionService.impl.method.syncVersionRuntime;

import com.omgservers.dto.tenant.SyncVersionRuntimeRequest;
import com.omgservers.dto.tenant.SyncVersionRuntimeResponse;
import com.omgservers.module.tenant.impl.operation.upsertVersionRuntime.UpsertVersionRuntimeOperation;
import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncVersionRuntimeMethodImpl implements SyncVersionRuntimeMethod {

    final UpsertVersionRuntimeOperation upsertVersionRuntimeOperation;
    final ChangeWithContextOperation changeWithContextOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<SyncVersionRuntimeResponse> syncVersionRuntime(final SyncVersionRuntimeRequest request) {
        final var versionRuntime = request.getVersionRuntime();

        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeWithContextOperation.<Boolean>changeWithContext(
                                (changeContext, sqlConnection) ->
                                        upsertVersionRuntimeOperation.upsertStageRuntime(changeContext,
                                                sqlConnection,
                                                shardModel.shard(),
                                                versionRuntime
                                        )
                        )
                        .map(ChangeContext::getResult))
                .map(SyncVersionRuntimeResponse::new);
    }
}
