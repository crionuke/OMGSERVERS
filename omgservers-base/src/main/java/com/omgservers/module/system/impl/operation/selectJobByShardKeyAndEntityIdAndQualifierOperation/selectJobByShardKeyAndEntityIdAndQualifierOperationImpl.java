package com.omgservers.module.system.impl.operation.selectJobByShardKeyAndEntityIdAndQualifierOperation;

import com.omgservers.model.job.JobModel;
import com.omgservers.model.job.JobQualifierEnum;
import com.omgservers.module.system.impl.mappers.JobModelMapper;
import com.omgservers.operation.executeSelectObject.ExecuteSelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class selectJobByShardKeyAndEntityIdAndQualifierOperationImpl
        implements SelectJobByShardKeyAndEntityIdAndQualifierOperation {

    final ExecuteSelectObjectOperation executeSelectObjectOperation;

    final JobModelMapper jobModelMapper;

    @Override
    public Uni<JobModel> selectJobByShardKeyAndEntityIdAndQualifier(final SqlConnection sqlConnection,
                                                                    final Long shardKey,
                                                                    final Long entityId,
                                                                    final JobQualifierEnum qualifier) {
        return executeSelectObjectOperation.executeSelectObject(
                sqlConnection,
                0,
                """
                        select id, created, shard_key, entity_id, qualifier
                        from system.tab_job
                        where shard_key = $1 and entity_id = $2 and qualifier = $3
                        """,
                Arrays.asList(
                        shardKey,
                        entityId,
                        qualifier
                ),
                "Job",
                jobModelMapper::fromRow);
    }
}
