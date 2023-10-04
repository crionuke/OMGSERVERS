package com.omgservers.module.system.impl.operation.selectAllJobs;

import com.omgservers.model.job.JobModel;
import com.omgservers.module.system.impl.mappers.JobModelMapper;
import com.omgservers.operation.selectList.SelectListOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SelectAllJobsOperationImpl implements SelectAllJobsOperation {

    final SelectListOperation selectListOperation;

    final JobModelMapper jobModelMapper;

    @Override
    public Uni<List<JobModel>> selectAllJobs(SqlConnection sqlConnection) {
        return selectListOperation.selectList(
                sqlConnection,
                0,
                """
                        select id, created, shard_key, entity_id, qualifier
                        from system.tab_job
                        """,
                List.of(),
                "Job",
                jobModelMapper::fromRow);
    }
}
