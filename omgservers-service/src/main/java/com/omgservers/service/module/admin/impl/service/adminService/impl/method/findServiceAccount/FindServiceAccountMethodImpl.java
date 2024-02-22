package com.omgservers.service.module.admin.impl.service.adminService.impl.method.findServiceAccount;

import com.omgservers.model.dto.admin.FindServiceAccountAdminRequest;
import com.omgservers.model.dto.admin.FindServiceAccountAdminResponse;
import com.omgservers.model.dto.system.FindServiceAccountRequest;
import com.omgservers.model.dto.system.FindServiceAccountResponse;
import com.omgservers.model.serviceAccount.ServiceAccountModel;
import com.omgservers.service.factory.ServiceAccountModelFactory;
import com.omgservers.service.module.system.SystemModule;
import com.omgservers.service.operation.getConfig.GetConfigOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class FindServiceAccountMethodImpl implements FindServiceAccountMethod {

    final SystemModule systemModule;

    final GetConfigOperation getConfigOperation;

    final ServiceAccountModelFactory serviceAccountModelFactory;

    @Override
    public Uni<FindServiceAccountAdminResponse> findServiceAccount(final FindServiceAccountAdminRequest request) {
        log.debug("Find service account, request={}", request);

        final var username = request.getUsername();
        return findServiceAccount(username)
                .map(FindServiceAccountAdminResponse::new);
    }

    Uni<ServiceAccountModel> findServiceAccount(final String username) {
        final var request = new FindServiceAccountRequest(username);
        return systemModule.getServiceAccountService().findServiceAccount(request)
                .map(FindServiceAccountResponse::getServiceAccount);
    }
}
