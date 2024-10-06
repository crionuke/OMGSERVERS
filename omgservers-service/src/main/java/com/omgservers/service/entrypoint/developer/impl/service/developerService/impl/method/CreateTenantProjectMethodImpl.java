package com.omgservers.service.entrypoint.developer.impl.service.developerService.impl.method;

import com.omgservers.schema.entrypoint.developer.CreateTenantProjectDeveloperRequest;
import com.omgservers.schema.entrypoint.developer.CreateTenantProjectDeveloperResponse;
import com.omgservers.schema.model.project.TenantProjectModel;
import com.omgservers.schema.model.tenantPermission.TenantPermissionQualifierEnum;
import com.omgservers.schema.model.tenantProjectPermission.TenantProjectPermissionQualifierEnum;
import com.omgservers.schema.model.tenantStage.TenantStageModel;
import com.omgservers.schema.model.tenantStagePermission.TenantStagePermissionQualifierEnum;
import com.omgservers.schema.module.tenant.tenantProject.SyncTenantProjectRequest;
import com.omgservers.schema.module.tenant.tenantStage.SyncTenantStageRequest;
import com.omgservers.service.entrypoint.developer.impl.service.developerService.impl.operation.CheckTenantPermissionOperation;
import com.omgservers.service.entrypoint.developer.impl.service.developerService.impl.operation.CreateTenantProjectPermissionOperation;
import com.omgservers.service.entrypoint.developer.impl.service.developerService.impl.operation.CreateTenantStagePermissionOperation;
import com.omgservers.service.factory.tenant.TenantProjectModelFactory;
import com.omgservers.service.factory.tenant.TenantProjectPermissionModelFactory;
import com.omgservers.service.factory.tenant.TenantStageModelFactory;
import com.omgservers.service.factory.tenant.TenantStagePermissionModelFactory;
import com.omgservers.service.module.tenant.TenantModule;
import com.omgservers.service.module.user.UserModule;
import com.omgservers.service.operation.generateId.GenerateIdOperation;
import com.omgservers.service.security.ServiceSecurityAttributes;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CreateTenantProjectMethodImpl implements CreateTenantProjectMethod {

    final TenantModule tenantModule;
    final UserModule userModule;

    final CreateTenantProjectPermissionOperation createTenantProjectPermissionOperation;
    final CreateTenantStagePermissionOperation createTenantStagePermissionOperation;
    final CheckTenantPermissionOperation checkTenantPermissionOperation;
    final GenerateIdOperation generateIdOperation;

    final TenantProjectPermissionModelFactory tenantProjectPermissionModelFactory;
    final TenantStagePermissionModelFactory tenantStagePermissionModelFactory;
    final TenantProjectModelFactory tenantProjectModelFactory;
    final TenantStageModelFactory tenantStageModelFactory;

    final SecurityIdentity securityIdentity;

    @Override
    public Uni<CreateTenantProjectDeveloperResponse> execute(final CreateTenantProjectDeveloperRequest request) {
        log.debug("Create tenant project, request={}", request);

        final var userId = securityIdentity.<Long>getAttribute(ServiceSecurityAttributes.USER_ID.getAttributeName());
        final var tenantId = request.getTenantId();

        final var permissionQualifier = TenantPermissionQualifierEnum.PROJECT_MANAGEMENT;
        return checkTenantPermissionOperation.execute(tenantId, userId, permissionQualifier)
                .flatMap(voidItem -> createTenantProject(tenantId, userId)
                        .flatMap(tenantProject -> createTenantStage(tenantId, tenantProject.getId(), userId)
                                .map(tenantStage -> {
                                    final var tenantProjectId = tenantProject.getId();
                                    final var tenantStageId = tenantStage.getId();
                                    final var tenantStageSecret = tenantStage.getSecret();
                                    return new CreateTenantProjectDeveloperResponse(tenantProjectId,
                                            tenantStageId,
                                            tenantStageSecret);
                                })));
    }

    Uni<TenantProjectModel> createTenantProject(final Long tenantId, final Long userId) {
        final var tenantProject = tenantProjectModelFactory.create(tenantId);
        final var tenantProjectId = tenantProject.getId();
        final var request = new SyncTenantProjectRequest(tenantProject);
        return tenantModule.getTenantService().syncTenantProject(request)
                .flatMap(response -> createTenantProjectPermissionOperation.execute(tenantId, tenantProjectId, userId,
                        TenantProjectPermissionQualifierEnum.STAGE_MANAGEMENT))
                .flatMap(response -> createTenantProjectPermissionOperation.execute(tenantId, tenantProjectId, userId,
                        TenantProjectPermissionQualifierEnum.VERSION_MANAGEMENT))
                .flatMap(response -> createTenantProjectPermissionOperation.execute(tenantId, tenantProjectId, userId,
                        TenantProjectPermissionQualifierEnum.GETTING_DASHBOARD))
                .replaceWith(tenantProject);
    }

    Uni<TenantStageModel> createTenantStage(final Long tenantId,
                                            final Long tenantProjectId,
                                            final Long userId) {
        final var tenantStage = tenantStageModelFactory.create(tenantId, tenantProjectId);
        final var tenantStageId = tenantStage.getId();
        final var request = new SyncTenantStageRequest(tenantStage);
        return tenantModule.getTenantService().syncTenantStage(request)
                .flatMap(response -> createTenantStagePermissionOperation.execute(tenantId, tenantStageId, userId,
                        TenantStagePermissionQualifierEnum.DEPLOYMENT_MANAGEMENT))
                .replaceWith(tenantStage);
    }
}
