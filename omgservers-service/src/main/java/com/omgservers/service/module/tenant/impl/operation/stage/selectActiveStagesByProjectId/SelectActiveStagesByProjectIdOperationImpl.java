package com.omgservers.service.module.tenant.impl.operation.stage.selectActiveStagesByProjectId;

import com.omgservers.schema.model.stage.StageModel;
import com.omgservers.service.module.tenant.impl.mapper.StageModelMapper;
import com.omgservers.service.operation.selectList.SelectListOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectActiveStagesByProjectIdOperationImpl implements SelectActiveStagesByProjectIdOperation {

    final SelectListOperation selectListOperation;

    final StageModelMapper stageModelMapper;

    @Override
    public Uni<List<StageModel>> selectActiveStagesByProjectId(final SqlConnection sqlConnection,
                                                               final int shard,
                                                               final Long tenantId,
                                                               final Long projectId) {
        return selectListOperation.selectList(
                sqlConnection,
                shard,
                """
                        select id, idempotency_key, tenant_id, project_id, created, modified, secret, deleted
                        from $schema.tab_tenant_stage
                        where tenant_id = $1 and project_id = $2 and deleted = false
                        order by id asc
                        """,
                List.of(
                        tenantId,
                        projectId
                ),
                "Stage",
                stageModelMapper::fromRow);
    }
}
