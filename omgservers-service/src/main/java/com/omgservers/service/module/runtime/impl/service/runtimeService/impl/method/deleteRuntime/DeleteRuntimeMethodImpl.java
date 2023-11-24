package com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.deleteRuntime;

import com.omgservers.model.dto.runtime.DeleteRuntimeRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeResponse;
import com.omgservers.model.shard.ShardModel;
import com.omgservers.service.module.runtime.impl.operation.deleteRuntime.DeleteRuntimeOperation;
import com.omgservers.service.module.system.SystemModule;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import com.omgservers.service.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class DeleteRuntimeMethodImpl implements DeleteRuntimeMethod {

    final SystemModule systemModule;

    final ChangeWithContextOperation changeWithContextOperation;
    final DeleteRuntimeOperation deleteRuntimeOperation;
    final CheckShardOperation checkShardOperation;

    @Override
    public Uni<DeleteRuntimeResponse> deleteRuntime(DeleteRuntimeRequest request) {
        log.debug("Delete runtime, request={}", request);

        final var id = request.getId();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, id))
                .map(DeleteRuntimeResponse::new);
    }

    Uni<Boolean> changeFunction(ShardModel shardModel, Long id) {
        return changeWithContextOperation.<Boolean>changeWithContext((changeContext, sqlConnection) ->
                        deleteRuntimeOperation.deleteRuntime(
                                changeContext,
                                sqlConnection,
                                shardModel.shard(),
                                id))
                .map(ChangeContext::getResult);
    }
}
