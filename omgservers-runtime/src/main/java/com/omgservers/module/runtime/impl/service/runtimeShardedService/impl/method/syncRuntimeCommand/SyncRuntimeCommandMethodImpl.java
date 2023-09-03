package com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncRuntimeCommand;

import com.omgservers.dto.runtime.SyncRuntimeCommandShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeCommandShardedResponse;
import com.omgservers.model.runtimeCommand.RuntimeCommandModel;
import com.omgservers.model.shard.ShardModel;
import com.omgservers.module.runtime.impl.operation.upsertRuntimeCommand.UpsertRuntimeCommandOperation;
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
class SyncRuntimeCommandMethodImpl implements SyncRuntimeCommandMethod {

    final ChangeWithContextOperation changeWithContextOperation;
    final UpsertRuntimeCommandOperation upsertRuntimeCommandOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<SyncRuntimeCommandShardedResponse> syncRuntimeCommand(SyncRuntimeCommandShardedRequest request) {
        SyncRuntimeCommandShardedRequest.validate(request);

        final var runtimeCommand = request.getRuntimeCommand();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, runtimeCommand))
                .map(SyncRuntimeCommandShardedResponse::new);
    }

    Uni<Boolean> changeFunction(ShardModel shardModel, RuntimeCommandModel runtimeCommand) {
        return changeWithContextOperation.changeWithContext((changeContext, sqlConnection) ->
                upsertRuntimeCommandOperation.upsertRuntimeCommand(
                        changeContext,
                        sqlConnection,
                        shardModel.shard(),
                        runtimeCommand));
    }
}
