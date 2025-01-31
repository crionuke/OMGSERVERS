package com.omgservers.service.shard.tenant.impl.service.tenantService.impl.method.tenantFilesArchive;

import com.omgservers.schema.module.tenant.tenantFilesArchive.DeleteTenantFilesArchiveRequest;
import com.omgservers.schema.module.tenant.tenantFilesArchive.DeleteTenantFilesArchiveResponse;
import com.omgservers.service.shard.tenant.impl.operation.tenantFilesArchive.DeleteTenantFilesArchiveOperation;
import com.omgservers.service.operation.server.ChangeContext;
import com.omgservers.service.operation.server.ChangeWithContextOperation;
import com.omgservers.service.operation.server.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteTenantFilesArchiveMethodImpl implements DeleteTenantFilesArchiveMethod {

    final DeleteTenantFilesArchiveOperation deleteTenantFilesArchiveOperation;
    final ChangeWithContextOperation changeWithContextOperation;
    final CheckShardOperation checkShardOperation;

    @Override
    public Uni<DeleteTenantFilesArchiveResponse> execute(
            final DeleteTenantFilesArchiveRequest request) {
        log.trace("{}", request);

        final var tenantId = request.getTenantId();
        final var id = request.getId();

        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> changeWithContextOperation.<Boolean>changeWithContext(
                                (changeContext, sqlConnection) ->
                                        deleteTenantFilesArchiveOperation.execute(changeContext,
                                                sqlConnection,
                                                shardModel.shard(),
                                                tenantId,
                                                id))
                        .map(ChangeContext::getResult))
                .map(DeleteTenantFilesArchiveResponse::new);
    }
}
