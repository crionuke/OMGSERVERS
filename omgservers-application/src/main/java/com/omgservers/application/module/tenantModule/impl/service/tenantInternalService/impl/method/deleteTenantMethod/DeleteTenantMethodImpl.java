package com.omgservers.application.module.tenantModule.impl.service.tenantInternalService.impl.method.deleteTenantMethod;

import com.omgservers.base.factory.LogModelFactory;
import com.omgservers.application.module.tenantModule.impl.operation.deleteTenantOperation.DeleteTenantOperation;
import com.omgservers.base.impl.operation.changeOperation.ChangeOperation;
import com.omgservers.dto.tenantModule.DeleteTenantInternalRequest;
import com.omgservers.model.event.body.TenantDeletedEventBodyModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteTenantMethodImpl implements DeleteTenantMethod {

    final DeleteTenantOperation deleteTenantOperation;
    final ChangeOperation changeOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<Void> deleteTenant(final DeleteTenantInternalRequest request) {
        DeleteTenantInternalRequest.validate(request);

        final var id = request.getId();
        return changeOperation.changeWithEvent(request,
                        ((sqlConnection, shardModel) -> deleteTenantOperation
                                .deleteTenant(sqlConnection, shardModel.shard(), id)),
                        deleted -> {
                            if (deleted) {
                                return logModelFactory.create("Tenant was deleted, id=" + id);
                            } else {
                                return null;
                            }
                        },
                        deleted -> {
                            if (deleted) {
                                return new TenantDeletedEventBodyModel(id);
                            } else {
                                return null;
                            }
                        })
                //TODO: implement response with deleted flag
                .replaceWithVoid();
    }
}
