package com.omgservers.service.shard.tenant.impl.service.tenantService.impl.method.tenantMatchmakerRequest;

import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.GetTenantMatchmakerRequestRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.GetTenantMatchmakerRequestResponse;
import com.omgservers.service.shard.tenant.impl.operation.tenantMatchmakerRequest.SelectTenantMatchmakerRequestOperation;
import com.omgservers.service.operation.server.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetTenantMatchmakerRequestMethodImpl implements GetTenantMatchmakerRequestMethod {

    final SelectTenantMatchmakerRequestOperation selectTenantMatchmakerRequestOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<GetTenantMatchmakerRequestResponse> execute(
            final GetTenantMatchmakerRequestRequest request) {
        log.trace("{}", request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var shard = shardModel.shard();
                    final var tenantId = request.getTenantId();
                    final var id = request.getId();
                    return pgPool.withTransaction(sqlConnection -> selectTenantMatchmakerRequestOperation
                            .execute(sqlConnection, shard, tenantId, id));
                })
                .map(GetTenantMatchmakerRequestResponse::new);
    }
}
