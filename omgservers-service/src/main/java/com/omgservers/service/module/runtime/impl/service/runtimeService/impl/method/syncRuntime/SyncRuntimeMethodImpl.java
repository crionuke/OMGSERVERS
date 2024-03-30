package com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.syncRuntime;

import com.omgservers.model.dto.runtime.SyncRuntimeRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeResponse;
import com.omgservers.model.runtime.RuntimeModel;
import com.omgservers.model.runtimeCommand.body.InitRuntimeCommandBodyModel;
import com.omgservers.model.shard.ShardModel;
import com.omgservers.service.factory.RuntimeCommandModelFactory;
import com.omgservers.service.module.runtime.impl.operation.runtime.upsertRuntime.UpsertRuntimeOperation;
import com.omgservers.service.module.runtime.impl.operation.runtimeCommand.upsertRuntimeCommand.UpsertRuntimeCommandOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import com.omgservers.service.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
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
    public Uni<SyncRuntimeResponse> syncRuntime(SyncRuntimeRequest request) {
        log.debug("Sync lobby runtime, request={}", request);

        final var runtime = request.getRuntime();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, runtime))
                .map(SyncRuntimeResponse::new);
    }

    Uni<Boolean> changeFunction(final ShardModel shardModel, final RuntimeModel runtime) {
        final int shard = shardModel.shard();
        return changeWithContextOperation.<Boolean>changeWithContext((changeContext, sqlConnection) ->
                        upsertRuntimeOperation.upsertRuntime(
                                        changeContext,
                                        sqlConnection,
                                        shard,
                                        runtime)
                                .call(inserted -> {
                                    if (inserted) {
                                        // InitRuntime is always first command of runtime
                                        final var commandBody = InitRuntimeCommandBodyModel.builder()
                                                .config(runtime.getConfig())
                                                .build();
                                        final var initCommand = runtimeCommandModelFactory
                                                .create(runtime.getId(), commandBody);
                                        return upsertRuntimeCommandOperation.upsertRuntimeCommand(
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
