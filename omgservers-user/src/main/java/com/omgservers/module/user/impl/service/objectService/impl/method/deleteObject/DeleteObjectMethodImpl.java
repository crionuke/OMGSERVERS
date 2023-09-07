package com.omgservers.module.user.impl.service.objectService.impl.method.deleteObject;

import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.dto.user.DeleteObjectRequest;
import com.omgservers.dto.user.DeleteObjectResponse;
import com.omgservers.module.user.impl.operation.deleteObject.DeleteObjectOperation;
import com.omgservers.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteObjectMethodImpl implements DeleteObjectMethod {

    final ChangeWithContextOperation changeWithContextOperation;
    final DeleteObjectOperation deleteObjectOperation;
    final CheckShardOperation checkShardOperation;

    @Override
    public Uni<DeleteObjectResponse> deleteObject(final DeleteObjectRequest request) {
        DeleteObjectRequest.validate(request);

        final var userId = request.getUserId();
        final var playerId = request.getPlayerId();
        final var id = request.getId();
        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> changeWithContextOperation.<Boolean>changeWithContext(
                        (changeContext, sqlConnection) -> deleteObjectOperation.deleteObject(
                                changeContext,
                                sqlConnection,
                                shardModel.shard(),
                                userId,
                                playerId,
                                id)))
                .map(ChangeContext::getResult)
                .map(DeleteObjectResponse::new);
    }
}
