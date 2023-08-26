package com.omgservers.application.module.tenantModule.impl.service.tenantInternalService.impl.method.syncTenantMethod;

import com.omgservers.application.module.tenantModule.impl.operation.upsertTenantOperation.UpsertTenantOperation;
import com.omgservers.application.module.tenantModule.impl.operation.validateTenantOperation.ValidateTenantOperation;
import com.omgservers.base.factory.LogModelFactory;
import com.omgservers.base.module.internal.InternalModule;
import com.omgservers.dto.internalModule.ChangeWithEventRequest;
import com.omgservers.dto.internalModule.ChangeWithEventResponse;
import com.omgservers.dto.tenantModule.SyncTenantRoutedRequest;
import com.omgservers.dto.tenantModule.SyncTenantResponse;
import com.omgservers.model.event.body.TenantCreatedEventBodyModel;
import com.omgservers.model.event.body.TenantUpdatedEventBodyModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncTenantMethodImpl implements SyncTenantMethod {

    final InternalModule internalModule;

    final ValidateTenantOperation validateTenantOperation;
    final UpsertTenantOperation upsertTenantOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<SyncTenantResponse> syncTenant(SyncTenantRoutedRequest request) {
        SyncTenantRoutedRequest.validate(request);

        final var tenant = request.getTenant();
        validateTenantOperation.validateTenant(tenant);
        return internalModule.getChangeService().changeWithEvent(new ChangeWithEventRequest(request,
                        (sqlConnection, shardModel) -> upsertTenantOperation
                                .upsertTenant(sqlConnection, shardModel.shard(), tenant),
                        inserted -> {
                            if (inserted) {
                                return logModelFactory.create("Tenant was created, tenant=" + tenant);
                            } else {
                                return logModelFactory.create("Tenant was updated, tenant=" + tenant);
                            }
                        },
                        inserted -> {
                            final var id = tenant.getId();
                            if (inserted) {
                                return new TenantCreatedEventBodyModel(id);
                            } else {
                                return new TenantUpdatedEventBodyModel(id);
                            }
                        }
                ))
                .map(ChangeWithEventResponse::getResult)
                .map(SyncTenantResponse::new);
    }
}