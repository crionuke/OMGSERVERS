package com.omgservers.schema.entrypoint.support;

import com.omgservers.schema.model.tenantProjectPermission.TenantProjectPermissionQualifierEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProjectPermissionsSupportRequest {

    @NotNull
    Long tenantId;

    @NotNull
    Long tenantProjectId;

    @NotNull
    Long userId;

    @NotEmpty
    Set<TenantProjectPermissionQualifierEnum> permissionsToDelete;
}
