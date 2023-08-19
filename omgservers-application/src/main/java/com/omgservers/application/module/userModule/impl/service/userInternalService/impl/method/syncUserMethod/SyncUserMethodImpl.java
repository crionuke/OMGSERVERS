package com.omgservers.application.module.userModule.impl.service.userInternalService.impl.method.syncUserMethod;

import com.omgservers.application.module.internalModule.InternalModule;
import com.omgservers.application.module.internalModule.impl.service.logHelpService.request.SyncLogHelpRequest;
import com.omgservers.application.module.internalModule.model.log.LogModel;
import com.omgservers.application.module.internalModule.model.log.LogModelFactory;
import com.omgservers.application.operation.checkShardOperation.CheckShardOperation;
import com.omgservers.application.module.userModule.impl.operation.upsertUserOperation.UpsertUserOperation;
import com.omgservers.application.module.userModule.impl.service.userInternalService.request.SyncUserInternalRequest;
import com.omgservers.application.module.userModule.impl.service.userInternalService.response.SyncUserInternalResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncUserMethodImpl implements SyncUserMethod {

    final InternalModule internalModule;

    final CheckShardOperation checkShardOperation;
    final UpsertUserOperation upsertUserOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<SyncUserInternalResponse> syncUser(final SyncUserInternalRequest request) {
        SyncUserInternalRequest.validate(request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var user = request.getUser();
                    return pgPool.withTransaction(sqlConnection ->
                            upsertUserOperation.upsertUser(sqlConnection, shardModel.shard(), user)
                                    .call(inserted -> {
                                        final LogModel syncLog;
                                        if (inserted) {
                                            syncLog = logModelFactory.create("User was created, user=" + user);
                                        } else {
                                            syncLog = logModelFactory.create("User was updated, user=" + user);
                                        }
                                        final var syncLogHelpRequest = new SyncLogHelpRequest(syncLog);
                                        return internalModule.getLogHelpService().syncLog(syncLogHelpRequest);
                                    }));
                })
                .map(SyncUserInternalResponse::new);
    }
}
