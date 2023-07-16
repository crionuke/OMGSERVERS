package com.omgservers.application.module.internalModule.impl.operation.selectAllJobsOperation;

import com.omgservers.application.module.internalModule.model.job.JobModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.time.Duration;
import java.util.List;

public interface SelectAllJobsOperation {
    Uni<List<JobModel>> selectAllJobs(SqlConnection sqlConnection);

    default List<JobModel> selectAllJobs(long timeout, PgPool pgPool) {
        return pgPool.withTransaction(this::selectAllJobs)
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
