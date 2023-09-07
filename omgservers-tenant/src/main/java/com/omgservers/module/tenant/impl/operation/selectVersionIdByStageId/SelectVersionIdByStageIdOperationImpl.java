package com.omgservers.module.tenant.impl.operation.selectVersionIdByStageId;

import com.omgservers.operation.executeSelectObject.ExecuteSelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectVersionIdByStageIdOperationImpl implements SelectVersionIdByStageIdOperation {

    private static final String SQL = """
                        
            """;

    final ExecuteSelectObjectOperation executeSelectObjectOperation;

    @Override
    public Uni<Long> selectVersionIdByStageId(final SqlConnection sqlConnection,
                                              final int shard,
                                              final Long stageId) {
        return executeSelectObjectOperation.executeSelectObject(
                sqlConnection,
                shard,
                """
                        select id
                        from $schema.tab_tenant_version
                        where stage_id = $1
                        order by id desc
                        limit 1
                        """,
                Collections.singletonList(stageId),
                "Version",
                row -> row.getLong("id"));
    }
}
