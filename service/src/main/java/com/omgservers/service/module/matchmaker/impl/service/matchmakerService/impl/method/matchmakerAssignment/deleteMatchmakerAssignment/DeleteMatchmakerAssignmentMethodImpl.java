package com.omgservers.service.module.matchmaker.impl.service.matchmakerService.impl.method.matchmakerAssignment.deleteMatchmakerAssignment;

import com.omgservers.schema.module.matchmaker.DeleteMatchmakerAssignmentRequest;
import com.omgservers.schema.module.matchmaker.DeleteMatchmakerAssignmentResponse;
import com.omgservers.service.module.matchmaker.impl.operation.matchmakerAssignment.deleteMatchmakerAssignment.DeleteMatchmakerAssignmentOperation;
import com.omgservers.service.server.operation.changeWithContext.ChangeContext;
import com.omgservers.service.server.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.service.server.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class DeleteMatchmakerAssignmentMethodImpl implements DeleteMatchmakerAssignmentMethod {

    final DeleteMatchmakerAssignmentOperation deleteMatchmakerAssignmentOperation;
    final ChangeWithContextOperation changeWithContextOperation;
    final CheckShardOperation checkShardOperation;

    @Override
    public Uni<DeleteMatchmakerAssignmentResponse> deleteMatchmakerAssignment(
            final DeleteMatchmakerAssignmentRequest request) {
        log.debug("Delete matchmaker assignment, request={}", request);

        final var matchmakerId = request.getMatchmakerId();
        final var id = request.getId();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeWithContextOperation.<Boolean>changeWithContext(
                                (changeContext, sqlConnection) -> deleteMatchmakerAssignmentOperation
                                        .deleteMatchmakerAssignment(changeContext,
                                                sqlConnection,
                                                shardModel.shard(),
                                                matchmakerId,
                                                id))
                        .map(ChangeContext::getResult))
                .map(DeleteMatchmakerAssignmentResponse::new);
    }
}
