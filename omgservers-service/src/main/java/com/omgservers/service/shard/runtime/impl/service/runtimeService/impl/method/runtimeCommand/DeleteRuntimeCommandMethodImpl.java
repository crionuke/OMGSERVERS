package com.omgservers.service.shard.runtime.impl.service.runtimeService.impl.method.runtimeCommand;

import com.omgservers.schema.model.shard.ShardModel;
import com.omgservers.schema.module.runtime.DeleteRuntimeCommandRequest;
import com.omgservers.schema.module.runtime.DeleteRuntimeCommandResponse;
import com.omgservers.service.shard.runtime.impl.operation.runtimeCommand.DeleteRuntimeCommandOperation;
import com.omgservers.service.operation.server.ChangeContext;
import com.omgservers.service.operation.server.ChangeWithContextOperation;
import com.omgservers.service.operation.server.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class DeleteRuntimeCommandMethodImpl implements DeleteRuntimeCommandMethod {

    final ChangeWithContextOperation changeWithContextOperation;
    final DeleteRuntimeCommandOperation deleteRuntimeCommandOperation;
    final CheckShardOperation checkShardOperation;

    @Override
    public Uni<DeleteRuntimeCommandResponse> execute(DeleteRuntimeCommandRequest request) {
        log.trace("{}", request);

        final var runtimeId = request.getRuntimeId();
        final var id = request.getId();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, runtimeId, id))
                .map(DeleteRuntimeCommandResponse::new);
    }

    Uni<Boolean> changeFunction(ShardModel shardModel, Long runtimeId, Long id) {
        return changeWithContextOperation.<Boolean>changeWithContext((changeContext, sqlConnection) ->
                        deleteRuntimeCommandOperation.execute(
                                changeContext,
                                sqlConnection,
                                shardModel.shard(),
                                runtimeId,
                                id))
                .map(ChangeContext::getResult);
    }
}
