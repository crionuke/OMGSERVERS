package com.omgservers.schema.module.tenant.tenantPermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyTenantPermissionExistsResponse {

    Boolean exists;
}
