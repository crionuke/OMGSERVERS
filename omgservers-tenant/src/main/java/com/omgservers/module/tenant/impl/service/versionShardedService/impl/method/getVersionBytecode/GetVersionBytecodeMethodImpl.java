package com.omgservers.module.tenant.impl.service.versionShardedService.impl.method.getVersionBytecode;

import com.omgservers.dto.tenant.GetVersionBytecodeShardedRequest;
import com.omgservers.dto.tenant.GetVersionBytecodeShardedResponse;
import com.omgservers.module.tenant.impl.operation.selectVersionBytecode.SelectVersionBytecodeOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetVersionBytecodeMethodImpl implements GetVersionBytecodeMethod {

    final SelectVersionBytecodeOperation selectVersionBytecodeOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<GetVersionBytecodeShardedResponse> getVersionBytecode(GetVersionBytecodeShardedRequest request) {
        GetVersionBytecodeShardedRequest.validate(request);

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var shard = shardModel.shard();
                    final var version = request.getVersionId();
                    return pgPool.withTransaction(sqlConnection -> selectVersionBytecodeOperation
                            .selectVersionBytecode(sqlConnection, shard, version));
                })
                .map(GetVersionBytecodeShardedResponse::new);
    }
}
