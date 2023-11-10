package com.omgservers.service.module.tenant.impl.service.versionService.impl.method.viewVersionRuntimes;

import com.omgservers.model.dto.tenant.ViewVersionRuntimesRequest;
import com.omgservers.model.dto.tenant.ViewVersionRuntimesResponse;
import com.omgservers.service.module.tenant.impl.operation.selectActiveVersionRuntimes.SelectActiveVersionRuntimes;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class ViewVersionRuntimesMethodImpl implements ViewVersionRuntimesMethod {

    final SelectActiveVersionRuntimes selectActiveVersionRuntimes;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<ViewVersionRuntimesResponse> viewVersionRuntimes(final ViewVersionRuntimesRequest request) {
        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shard -> {
                    final var tenantId = request.getTenantId();
                    final var versionId = request.getVersionId();
                    return pgPool.withTransaction(sqlConnection -> selectActiveVersionRuntimes
                            .selectActiveVersionRuntimes(sqlConnection,
                                    shard.shard(),
                                    tenantId,
                                    versionId
                            )
                    );
                })
                .map(ViewVersionRuntimesResponse::new);

    }
}
