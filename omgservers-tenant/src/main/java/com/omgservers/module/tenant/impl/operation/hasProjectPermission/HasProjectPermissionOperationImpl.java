package com.omgservers.module.tenant.impl.operation.hasProjectPermission;

import com.omgservers.model.projectPermission.ProjectPermissionEnum;
import com.omgservers.operation.executeHasObject.ExecuteHasObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class HasProjectPermissionOperationImpl implements HasProjectPermissionOperation {

    final ExecuteHasObjectOperation executeHasObjectOperation;

    @Override
    public Uni<Boolean> hasProjectPermission(final SqlConnection sqlConnection,
                                             final int shard,
                                             final Long tenantId,
                                             final Long projectId,
                                             final Long userId,
                                             final ProjectPermissionEnum permission) {
        return executeHasObjectOperation.executeHasObject(
                sqlConnection,
                shard,
                """
                        select id
                        from $schema.tab_tenant_project_permission
                        where tenant_id = $1 and project_id = $2 and user_id = $3 and permission = $4
                        limit 1
                        """,
                Arrays.asList(tenantId, projectId, userId, permission),
                "Project permission");
    }
}
