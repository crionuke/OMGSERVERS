package com.omgservers.module.system.impl.operation.getJobInterval;

import com.omgservers.model.job.JobQualifierEnum;

public interface GetJobIntervalOperation {
    Integer getJobIntervalInSeconds(JobQualifierEnum type);
}
