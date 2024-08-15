package com.omgservers.service.module.matchmaker.impl.service.matchmakerService.impl.method.matchmakerCommand.syncMatchmakerCommand;

import com.omgservers.schema.module.matchmaker.SyncMatchmakerCommandRequest;
import com.omgservers.schema.module.matchmaker.SyncMatchmakerCommandResponse;
import com.omgservers.schema.model.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.module.matchmaker.impl.operation.matchmaker.hasMatchmaker.HasMatchmakerOperation;
import com.omgservers.service.module.matchmaker.impl.operation.matchmakerCommand.upsertMatchmakerCommand.UpsertMatchmakerCommandOperation;
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
class SyncMatchmakerCommandMethodImpl implements SyncMatchmakerCommandMethod {

    final UpsertMatchmakerCommandOperation upsertMatchmakerCommandOperation;
    final ChangeWithContextOperation changeWithContextOperation;
    final HasMatchmakerOperation hasMatchmakerOperation;
    final CheckShardOperation checkShardOperation;

    @Override
    public Uni<SyncMatchmakerCommandResponse> syncMatchmakerCommand(final SyncMatchmakerCommandRequest request) {
        log.debug("Sync matchmaker command, request={}", request);

        final var matchmakerCommand = request.getMatchmakerCommand();
        final var matchmakerId = matchmakerCommand.getMatchmakerId();

        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> {
                    final var shard = shardModel.shard();
                    return changeWithContextOperation.<Boolean>changeWithContext(
                                    (changeContext, sqlConnection) -> hasMatchmakerOperation
                                            .hasMatchmaker(sqlConnection, shard, matchmakerId)
                                            .flatMap(has -> {
                                                if (has) {
                                                    return upsertMatchmakerCommandOperation
                                                            .upsertMatchmakerCommand(changeContext,
                                                                    sqlConnection,
                                                                    shardModel.shard(),
                                                                    matchmakerCommand);
                                                } else {
                                                    throw new ServerSideNotFoundException(
                                                            ExceptionQualifierEnum.PARENT_NOT_FOUND,
                                                            "matchmaker does not exist or was deleted, id=" + matchmakerId);
                                                }
                                            })

                            )
                            .map(ChangeContext::getResult);
                })
                .map(SyncMatchmakerCommandResponse::new);
    }
}
