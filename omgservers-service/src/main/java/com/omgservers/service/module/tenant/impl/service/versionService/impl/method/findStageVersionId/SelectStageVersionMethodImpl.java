package com.omgservers.service.module.tenant.impl.service.versionService.impl.method.findStageVersionId;

import com.omgservers.model.dto.tenant.SelectStageVersionRequest;
import com.omgservers.model.dto.tenant.SelectStageVersionResponse;
import com.omgservers.service.exception.ServerSideConflictException;
import com.omgservers.service.module.tenant.impl.operation.selectActiveVersionsByStageId.SelectActiveVersionsByStageIdOperation;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectStageVersionMethodImpl implements SelectStageVersionMethod {

    final SelectActiveVersionsByStageIdOperation selectActiveVersionsByStageIdOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<SelectStageVersionResponse> selectStageVersion(final SelectStageVersionRequest request) {
        log.debug("Select stage version, request={}", request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var tenantId = request.getTenantId();
                    final var stageId = request.getStageId();
                    return pgPool.withTransaction(sqlConnection -> selectActiveVersionsByStageIdOperation
                            .selectActiveVersionsByStageId(sqlConnection, shardModel.shard(), tenantId, stageId)
                            .map(versions -> {
                                if (versions.isEmpty()) {
                                    throw new ServerSideConflictException(String.format("Version was not select, " +
                                            "there aren't active stage versions, " +
                                            "stageId=%s", stageId));
                                }

                                final var strategy = request.getStrategy();
                                return switch (strategy) {
                                    case LAST -> versions.get(versions.size() - 1);
                                };
                            }));
                })
                .map(SelectStageVersionResponse::new);
    }
}
