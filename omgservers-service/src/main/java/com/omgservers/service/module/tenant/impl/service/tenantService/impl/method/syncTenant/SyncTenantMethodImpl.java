package com.omgservers.service.module.tenant.impl.service.tenantService.impl.method.syncTenant;

import com.omgservers.model.dto.tenant.SyncTenantRequest;
import com.omgservers.model.dto.tenant.SyncTenantResponse;
import com.omgservers.service.module.tenant.impl.operation.upsertTenant.UpsertTenantOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import com.omgservers.service.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncTenantMethodImpl implements SyncTenantMethod {

    final ChangeWithContextOperation changeWithContextOperation;
    final UpsertTenantOperation upsertTenantOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<SyncTenantResponse> syncTenant(final SyncTenantRequest request) {
        log.debug("Sync tenant, request={}", request);

        final var tenant = request.getTenant();
        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeWithContextOperation.<Boolean>changeWithContext(
                                (changeContext, sqlConnection) -> upsertTenantOperation
                                        .upsertTenant(changeContext, sqlConnection, shardModel.shard(), tenant))
                        .map(ChangeContext::getResult))
                .map(SyncTenantResponse::new);
    }
}