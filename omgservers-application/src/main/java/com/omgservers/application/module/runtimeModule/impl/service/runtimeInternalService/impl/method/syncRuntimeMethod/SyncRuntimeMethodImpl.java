package com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.impl.method.syncRuntimeMethod;

import com.omgservers.application.module.internalModule.InternalModule;
import com.omgservers.application.module.internalModule.model.event.body.RuntimeCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.RuntimeUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.log.LogModelFactory;
import com.omgservers.application.module.runtimeModule.impl.operation.upsertRuntimeOperation.UpsertRuntimeOperation;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.request.SyncRuntimeInternalRequest;
import com.omgservers.application.operation.changeOperation.ChangeOperation;
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
    final ChangeOperation changeOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<Void> syncRuntime(SyncRuntimeInternalRequest request) {
        SyncRuntimeInternalRequest.validate(request);

        final var runtime = request.getRuntime();
        return changeOperation.changeWithEvent(request,
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
                )
                //TODO: add response with created field
                .replaceWithVoid();
    }
}
