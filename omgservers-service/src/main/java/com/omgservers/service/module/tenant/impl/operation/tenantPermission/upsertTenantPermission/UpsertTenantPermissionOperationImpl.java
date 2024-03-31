package com.omgservers.service.module.tenant.impl.operation.tenantPermission.upsertTenantPermission;

import com.omgservers.model.tenantPermission.TenantPermissionModel;
import com.omgservers.service.factory.lobby.LogModelFactory;
import com.omgservers.service.operation.changeObject.ChangeObjectOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertTenantPermissionOperationImpl implements UpsertTenantPermissionOperation {

    final ChangeObjectOperation changeObjectOperation;
    final LogModelFactory logModelFactory;

    @Override
    public Uni<Boolean> upsertTenantPermission(final ChangeContext<?> changeContext,
                                               final SqlConnection sqlConnection,
                                               final int shard,
                                               final TenantPermissionModel tenantPermission) {
        return changeObjectOperation.changeObject(
                changeContext, sqlConnection, shard,
                """
                        insert into $schema.tab_tenant_permission(
                            id, idempotency_key, tenant_id, created, modified, user_id, permission, deleted)
                        values($1, $2, $3, $4, $5, $6, $7, $8)
                        on conflict (id) do
                        nothing
                        """,
                List.of(
                        tenantPermission.getId(),
                        tenantPermission.getIdempotencyKey(),
                        tenantPermission.getTenantId(),
                        tenantPermission.getCreated().atOffset(ZoneOffset.UTC),
                        tenantPermission.getModified().atOffset(ZoneOffset.UTC),
                        tenantPermission.getUserId(),
                        tenantPermission.getPermission(),
                        tenantPermission.getDeleted()
                ),
                () -> null,
                () -> null
        );
    }
}
