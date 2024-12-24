package com.omgservers.service.entrypoint.support.impl.service.supportService.impl.method;

import com.omgservers.schema.entrypoint.support.CreateTenantProjectPermissionsSupportRequest;
import com.omgservers.schema.entrypoint.support.CreateTenantProjectPermissionsSupportResponse;
import com.omgservers.schema.model.project.TenantProjectModel;
import com.omgservers.schema.model.tenant.TenantModel;
import com.omgservers.schema.model.tenantProjectPermission.TenantProjectPermissionQualifierEnum;
import com.omgservers.schema.model.user.UserModel;
import com.omgservers.schema.module.tenant.tenant.GetTenantRequest;
import com.omgservers.schema.module.tenant.tenant.GetTenantResponse;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectRequest;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectResponse;
import com.omgservers.schema.module.tenant.tenantProjectPermission.SyncTenantProjectPermissionRequest;
import com.omgservers.schema.module.tenant.tenantProjectPermission.SyncTenantProjectPermissionResponse;
import com.omgservers.schema.module.user.GetUserRequest;
import com.omgservers.schema.module.user.GetUserResponse;
import com.omgservers.service.factory.tenant.TenantProjectPermissionModelFactory;
import com.omgservers.service.module.tenant.TenantModule;
import com.omgservers.service.module.user.UserModule;
import com.omgservers.service.operation.getIdByProject.GetIdByProjectOperation;
import com.omgservers.service.operation.getIdByTenant.GetIdByTenantOperation;
import com.omgservers.service.security.ServiceSecurityAttributesEnum;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class CreateTenantProjectPermissionsMethodImpl implements CreateTenantProjectPermissionsMethod {

    final TenantModule tenantModule;
    final UserModule userModule;

    final GetIdByProjectOperation getIdByProjectOperation;
    final GetIdByTenantOperation getIdByTenantOperation;

    final TenantProjectPermissionModelFactory tenantProjectPermissionModelFactory;
    final SecurityIdentity securityIdentity;

    @Override
    public Uni<CreateTenantProjectPermissionsSupportResponse> execute(
            final CreateTenantProjectPermissionsSupportRequest request) {
        log.debug("Requested, {}, principal={}", request,
                securityIdentity.getPrincipal().getName());

        final var userId = securityIdentity
                .<Long>getAttribute(ServiceSecurityAttributesEnum.USER_ID.getAttributeName());

        return getIdByTenantOperation.execute(request.getTenant())
                .flatMap(tenantId -> getIdByProjectOperation.execute(tenantId, request.getProject())
                        .flatMap(tenantProjectId -> {
                            final var forUserId = request.getUserId();
                            return getUser(forUserId)
                                    .flatMap(user -> getTenant(tenantId)
                                            .flatMap(tenant -> getTenantProject(tenantId, tenantProjectId))
                                            .flatMap(project -> {
                                                final var permissionsToCreate = request.getPermissionsToCreate();
                                                return Multi.createFrom().iterable(permissionsToCreate)
                                                        .onItem().transformToUniAndConcatenate(permission ->
                                                                createTenantProjectPermission(tenantId,
                                                                        tenantProjectId, forUserId, permission)
                                                                        .map(created -> Tuple2.of(permission,
                                                                                created)))
                                                        .collect().asList();
                                            }))
                                    .map(results -> results.stream().filter(Tuple2::getItem2).map(Tuple2::getItem1)
                                            .toList())
                                    .invoke(createdPermissions -> {
                                        if (!createdPermissions.isEmpty()) {
                                            log.info("The \"{}\" project permissions in tenant \"{}\" " +
                                                            "for user \"{}\" were created by the user {}",
                                                    createdPermissions.size(), tenantId, forUserId, userId);
                                        }
                                    });
                        }))
                .map(CreateTenantProjectPermissionsSupportResponse::new);
    }

    Uni<UserModel> getUser(final Long id) {
        final var request = new GetUserRequest(id);
        return userModule.getService().getUser(request)
                .map(GetUserResponse::getUser);
    }

    Uni<TenantModel> getTenant(Long tenantId) {
        final var getTenantRequest = new GetTenantRequest(tenantId);
        return tenantModule.getService().getTenant(getTenantRequest)
                .map(GetTenantResponse::getTenant);
    }

    Uni<TenantProjectModel> getTenantProject(final Long tenantId, final Long id) {
        final var request = new GetTenantProjectRequest(tenantId, id);
        return tenantModule.getService().getTenantProject(request)
                .map(GetTenantProjectResponse::getTenantProject);
    }

    Uni<Boolean> createTenantProjectPermission(final Long tenantId,
                                               final Long tenantProjectId,
                                               final Long userId,
                                               final TenantProjectPermissionQualifierEnum permission) {
        return syncTenantProjectPermission(tenantId,
                tenantProjectId,
                userId,
                permission)
                .onFailure()
                .recoverWithUni(t -> {
                    log.warn("Sync project permission was failed, " +
                                    "tenantId={}, tenantProjectId={}, userId={}, permission={}, {}:{}",
                            tenantId,
                            tenantProjectId,
                            userId,
                            permission,
                            t.getClass().getSimpleName(),
                            t.getMessage());
                    return Uni.createFrom().item(Boolean.FALSE);
                });
    }

    Uni<Boolean> syncTenantProjectPermission(final Long tenantId,
                                             final Long tenantProjectId,
                                             final Long userId,
                                             final TenantProjectPermissionQualifierEnum permission) {
        final var tenantProjectPermission = tenantProjectPermissionModelFactory
                .create(tenantId, tenantProjectId, userId, permission);

        final var request = new SyncTenantProjectPermissionRequest(tenantProjectPermission);
        return tenantModule.getService().syncTenantProjectPermission(request)
                .map(SyncTenantProjectPermissionResponse::getCreated);
    }
}
