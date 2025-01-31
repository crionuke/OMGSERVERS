package com.omgservers.service.shard.runtime.impl.service.runtimeService.impl.method.runtime;

import com.omgservers.schema.model.runtime.RuntimeModel;
import com.omgservers.schema.model.runtimeCommand.body.InitRuntimeCommandBodyDto;
import com.omgservers.schema.model.shard.ShardModel;
import com.omgservers.schema.module.runtime.SyncRuntimeRequest;
import com.omgservers.schema.module.runtime.SyncRuntimeResponse;
import com.omgservers.service.factory.runtime.RuntimeCommandModelFactory;
import com.omgservers.service.shard.runtime.impl.operation.runtime.UpsertRuntimeOperation;
import com.omgservers.service.shard.runtime.impl.operation.runtimeCommand.UpsertRuntimeCommandOperation;
import com.omgservers.service.operation.server.ChangeContext;
import com.omgservers.service.operation.server.ChangeWithContextOperation;
import com.omgservers.service.operation.server.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncRuntimeMethodImpl implements SyncRuntimeMethod {

    final UpsertRuntimeCommandOperation upsertRuntimeCommandOperation;
    final ChangeWithContextOperation changeWithContextOperation;
    final UpsertRuntimeOperation upsertRuntimeOperation;
    final CheckShardOperation checkShardOperation;

    final RuntimeCommandModelFactory runtimeCommandModelFactory;

    @Override
    public Uni<SyncRuntimeResponse> execute(SyncRuntimeRequest request) {
        log.trace("{}", request);

        final var runtime = request.getRuntime();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, runtime))
                .map(SyncRuntimeResponse::new);
    }

    Uni<Boolean> changeFunction(final ShardModel shardModel, final RuntimeModel runtime) {
        final int shard = shardModel.shard();
        return changeWithContextOperation.<Boolean>changeWithContext((changeContext, sqlConnection) ->
                        upsertRuntimeOperation.execute(
                                        changeContext,
                                        sqlConnection,
                                        shard,
                                        runtime)
                                .call(inserted -> {
                                    if (inserted) {
                                        // InitRuntime is always first command of runtime
                                        final var commandBody = InitRuntimeCommandBodyDto.builder()
                                                .runtimeConfig(runtime.getConfig())
                                                .build();
                                        final var initCommand = runtimeCommandModelFactory
                                                .create(runtime.getId(), commandBody);
                                        return upsertRuntimeCommandOperation.execute(
                                                changeContext,
                                                sqlConnection,
                                                shard,
                                                initCommand);
                                    } else {
                                        return Uni.createFrom().voidItem();
                                    }
                                })
                )
                .map(ChangeContext::getResult);
    }
}
