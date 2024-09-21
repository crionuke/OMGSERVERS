package com.omgservers.service.module.tenant.impl.operation.tenantImageRef;

import com.omgservers.schema.model.tenantImageRef.TenantImageRefModel;
import com.omgservers.service.module.tenant.impl.mapper.TenantImageRefModelMapper;
import com.omgservers.service.operation.selectObject.SelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectTenantImageRefOperationImpl implements SelectTenantImageRefOperation {

    final SelectObjectOperation selectObjectOperation;

    final TenantImageRefModelMapper tenantImageRefModelMapper;

    @Override
    public Uni<TenantImageRefModel> execute(final SqlConnection sqlConnection,
                                            final int shard,
                                            final Long tenantId,
                                            final Long id) {
        return selectObjectOperation.selectObject(
                sqlConnection,
                shard,
                """
                        select
                            id, idempotency_key, tenant_id, version_id, created, modified, qualifier, image_id, deleted
                        from $schema.tab_tenant_image_ref
                        where tenant_id = $1 and id = $2
                        limit 1
                        """,
                List.of(tenantId, id),
                "Tenant image ref",
                tenantImageRefModelMapper::fromRow);
    }
}
