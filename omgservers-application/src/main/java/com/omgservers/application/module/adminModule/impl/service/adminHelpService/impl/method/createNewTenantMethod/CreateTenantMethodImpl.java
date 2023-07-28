package com.omgservers.application.module.adminModule.impl.service.adminHelpService.impl.method.createNewTenantMethod;

import com.omgservers.application.module.adminModule.impl.service.adminHelpService.request.CreateTenantHelpRequest;
import com.omgservers.application.module.adminModule.impl.service.adminHelpService.response.CreateTenantHelpResponse;
import com.omgservers.application.module.tenantModule.TenantModule;
import com.omgservers.application.module.tenantModule.impl.service.tenantInternalService.request.SyncTenantInternalRequest;
import com.omgservers.application.module.tenantModule.model.tenant.TenantConfigModel;
import com.omgservers.application.module.tenantModule.model.tenant.TenantModelFactory;
import com.omgservers.application.operation.generateIdOperation.GenerateIdOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class CreateTenantMethodImpl implements CreateTenantMethod {

    final TenantModule tenantModule;

    final TenantModelFactory tenantModelFactory;

    final GenerateIdOperation generateIdOperation;

    @Override
    public Uni<CreateTenantHelpResponse> createTenant(final CreateTenantHelpRequest request) {
        CreateTenantHelpRequest.validate(request);

        final var tenant = tenantModelFactory.create(TenantConfigModel.create());
        final var syncTenantInternalRequest = new SyncTenantInternalRequest(tenant);
        return tenantModule.getTenantInternalService().syncTenant(syncTenantInternalRequest)
                .replaceWith(new CreateTenantHelpResponse(tenant.getId()));
    }
}
