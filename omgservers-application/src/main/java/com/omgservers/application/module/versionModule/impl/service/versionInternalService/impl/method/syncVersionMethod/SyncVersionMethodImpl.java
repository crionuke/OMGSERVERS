package com.omgservers.application.module.versionModule.impl.service.versionInternalService.impl.method.syncVersionMethod;

import com.omgservers.base.factory.LogModelFactory;
import com.omgservers.application.module.versionModule.impl.operation.upsertVersionOperation.UpsertVersionOperation;
import com.omgservers.base.impl.operation.changeOperation.ChangeOperation;
import com.omgservers.dto.versionModule.SyncVersionInternalRequest;
import com.omgservers.dto.versionModule.SyncVersionInternalResponse;
import com.omgservers.model.event.body.VersionCreatedEventBodyModel;
import com.omgservers.model.event.body.VersionUpdatedEventBodyModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncVersionMethodImpl implements SyncVersionMethod {

    final UpsertVersionOperation upsertVersionOperation;
    final ChangeOperation changeOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<SyncVersionInternalResponse> syncVersion(SyncVersionInternalRequest request) {
        SyncVersionInternalRequest.validate(request);

        final var version = request.getVersion();
        return changeOperation.changeWithEvent(request,
                        (sqlConnection, shardModel) -> upsertVersionOperation
                                .upsertVersion(sqlConnection, shardModel.shard(), version),
                        inserted -> {
                            if (inserted) {
                                return logModelFactory.create("Version was created, version=" + version);
                            } else {
                                return logModelFactory.create("Version was updated, version=" + version);
                            }
                        },
                        inserted -> {
                            final var tenantId = version.getTenantId();
                            final var stageId = version.getStageId();
                            final var id = version.getId();
                            if (inserted) {
                                return new VersionCreatedEventBodyModel(tenantId, stageId, id);
                            } else {
                                return new VersionUpdatedEventBodyModel(tenantId, stageId, id);
                            }
                        }
                )
                .map(SyncVersionInternalResponse::new);
    }
}
