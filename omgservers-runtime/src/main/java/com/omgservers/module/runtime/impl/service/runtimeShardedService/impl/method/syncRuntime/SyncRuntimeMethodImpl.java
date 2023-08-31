package com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncRuntime;

import com.omgservers.dto.internal.ChangeWithEventRequest;
import com.omgservers.dto.internal.ChangeWithEventResponse;
import com.omgservers.dto.runtime.SyncRuntimeShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeShardedResponse;
import com.omgservers.module.internal.factory.LogModelFactory;
import com.omgservers.model.event.body.RuntimeCreatedEventBodyModel;
import com.omgservers.model.event.body.RuntimeUpdatedEventBodyModel;
import com.omgservers.module.internal.InternalModule;
import com.omgservers.module.runtime.impl.operation.upsertRuntime.UpsertRuntimeOperation;
import com.omgservers.operation.getConfig.GetConfigOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncRuntimeMethodImpl implements SyncRuntimeMethod {

    final InternalModule internalModule;

    final UpsertRuntimeOperation upsertRuntimeOperation;
    final GetConfigOperation getConfigOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<SyncRuntimeShardedResponse> syncRuntime(SyncRuntimeShardedRequest request) {
        SyncRuntimeShardedRequest.validate(request);

        final var runtime = request.getRuntime();
        return internalModule.getChangeService().changeWithEvent(new ChangeWithEventRequest(request,
                        (sqlConnection, shardModel) -> upsertRuntimeOperation
                                .upsertRuntime(sqlConnection, shardModel.shard(), runtime),
                        inserted -> {
                            if (inserted) {
                                return logModelFactory.create("Runtime was created, runtime=" + runtime);
                            } else {
                                return logModelFactory.create("Runtime was update, runtime=" + runtime);
                            }
                        },
                        inserted -> {
                            final var id = runtime.getId();
                            final var matchmakerId = runtime.getMatchmakerId();
                            final var matchId = runtime.getMatchId();
                            if (inserted) {
                                return new RuntimeCreatedEventBodyModel(id, matchmakerId, matchId);
                            } else {
                                return new RuntimeUpdatedEventBodyModel(id, matchmakerId, matchId);
                            }
                        }
                ))
                .map(this::prepareMethodResponse);
    }

    SyncRuntimeShardedResponse prepareMethodResponse(ChangeWithEventResponse changeWithEventResponse) {
        final var syncRuntimeShardedResponse = new SyncRuntimeShardedResponse();
        syncRuntimeShardedResponse.setCreated(changeWithEventResponse.getResult());
        if (getConfigOperation.getConfig().verbose()) {
            final var extendedResponse = new SyncRuntimeShardedResponse.ExtendedResponse();
            extendedResponse.setChangeExtendedResponse(changeWithEventResponse.getExtendedResponse());
            syncRuntimeShardedResponse.setExtendedResponse(extendedResponse);
        }
        return syncRuntimeShardedResponse;
    }
}
