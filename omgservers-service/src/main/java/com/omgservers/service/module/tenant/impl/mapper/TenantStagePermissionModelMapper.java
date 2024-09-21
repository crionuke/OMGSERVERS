package com.omgservers.service.module.tenant.impl.mapper;

import com.omgservers.schema.model.tenantStagePermission.TenantStagePermissionEnum;
import com.omgservers.schema.model.tenantStagePermission.TenantStagePermissionModel;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class TenantStagePermissionModelMapper {

    public TenantStagePermissionModel fromRow(final Row row) {
        final var tenantStagePermission = new TenantStagePermissionModel();
        tenantStagePermission.setId(row.getLong("id"));
        tenantStagePermission.setIdempotencyKey(row.getString("idempotency_key"));
        tenantStagePermission.setTenantId(row.getLong("tenant_id"));
        tenantStagePermission.setStageId(row.getLong("stage_id"));
        tenantStagePermission.setCreated(row.getOffsetDateTime("created").toInstant());
        tenantStagePermission.setModified(row.getOffsetDateTime("modified").toInstant());
        tenantStagePermission.setUserId(row.getLong("user_id"));
        tenantStagePermission.setPermission(TenantStagePermissionEnum.valueOf(row.getString("permission")));
        tenantStagePermission.setDeleted(row.getBoolean("deleted"));
        return tenantStagePermission;
    }
}
