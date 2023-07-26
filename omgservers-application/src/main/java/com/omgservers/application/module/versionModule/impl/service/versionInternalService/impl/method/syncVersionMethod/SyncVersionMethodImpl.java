package com.omgservers.application.module.versionModule.impl.service.versionInternalService.impl.method.syncVersionMethod;

import com.omgservers.application.module.internalModule.InternalModule;
import com.omgservers.application.module.internalModule.impl.service.eventHelpService.request.InsertEventHelpRequest;
import com.omgservers.application.module.internalModule.model.event.body.EventCreatedEventBodyModel;
import com.omgservers.application.module.versionModule.impl.operation.upsertVersionOperation.UpsertVersionOperation;
import com.omgservers.application.operation.checkShardOperation.CheckShardOperation;
import com.omgservers.application.module.versionModule.model.VersionModel;
import com.omgservers.application.module.internalModule.model.event.body.VersionCreatedEventBodyModel;
import com.omgservers.application.module.versionModule.impl.service.versionInternalService.request.SyncVersionInternalRequest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncVersionMethodImpl implements SyncVersionMethod {

    final InternalModule internalModule;
    final CheckShardOperation checkShardOperation;
    final UpsertVersionOperation upsertVersionOperation;
    final PgPool pgPool;

    @Override
    public Uni<Void> syncVersion(SyncVersionInternalRequest request) {
        SyncVersionInternalRequest.validate(request);

        final var version = request.getVersion();
        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> syncVersion(shardModel.shard(), version));
    }

    Uni<Void> syncVersion(Integer shard, VersionModel version) {
        return pgPool.withTransaction(sqlConnection ->
                        upsertVersionOperation.upsertVersion(sqlConnection, shard, version)
                                .flatMap(result -> {
                                    final var inserted = result.getItem2();
                                    if (inserted) {
                                        final var id = version.getId();
                                        final var tenantId = version.getTenantId();
                                        final var stageId = version.getStageId();
                                        final var eventBody = new VersionCreatedEventBodyModel(tenantId, stageId, id);
                                        final var insertEventInternalRequest = new InsertEventHelpRequest(sqlConnection, eventBody);
                                        return internalModule.getEventHelpService().insertEvent(insertEventInternalRequest);
                                    } else {
                                        return Uni.createFrom().voidItem();
                                    }
                                }))
                .replaceWithVoid();
    }
}
