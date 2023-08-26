package com.omgservers.application.module.userModule.impl.service.playerInternalService.impl.method.syncPlayerMethod;

import com.omgservers.application.module.userModule.impl.operation.upsertPlayerOperation.UpsertPlayerOperation;
import com.omgservers.application.module.userModule.impl.operation.validatePlayerOperation.ValidatePlayerOperation;
import com.omgservers.base.factory.LogModelFactory;
import com.omgservers.base.module.internal.InternalModule;
import com.omgservers.dto.internalModule.ChangeWithEventRequest;
import com.omgservers.dto.internalModule.ChangeWithEventResponse;
import com.omgservers.dto.userModule.SyncPlayerRoutedRequest;
import com.omgservers.dto.userModule.SyncPlayerInternalResponse;
import com.omgservers.model.event.body.PlayerCreatedEventBodyModel;
import com.omgservers.model.event.body.PlayerUpdatedEventBodyModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncPlayerMethodImpl implements SyncPlayerMethod {

    final InternalModule internalModule;

    final ValidatePlayerOperation validatePlayerOperation;
    final UpsertPlayerOperation upsertPlayerOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<SyncPlayerInternalResponse> syncPlayer(SyncPlayerRoutedRequest request) {
        SyncPlayerRoutedRequest.validate(request);

        final var player = request.getPlayer();
        final var userId = player.getUserId();
        return internalModule.getChangeService().changeWithEvent(new ChangeWithEventRequest(request,
                        (sqlConnection, shardModel) -> upsertPlayerOperation
                                .upsertPlayer(sqlConnection, shardModel.shard(), player),
                        inserted -> {
                            if (inserted) {
                                return logModelFactory.create("Player was created, player=" + player);
                            } else {
                                return logModelFactory.create("Player was updated, player=" + player);
                            }
                        },
                        inserted -> {
                            final var stageId = player.getStageId();
                            final var id = player.getId();
                            if (inserted) {
                                return new PlayerCreatedEventBodyModel(userId, stageId, id);
                            } else {
                                return new PlayerUpdatedEventBodyModel(userId, stageId, id);
                            }
                        }
                ))
                .map(ChangeWithEventResponse::getResult)
                .map(SyncPlayerInternalResponse::new);
    }
}
