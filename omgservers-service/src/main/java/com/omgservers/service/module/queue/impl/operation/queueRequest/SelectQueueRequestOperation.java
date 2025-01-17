package com.omgservers.service.module.queue.impl.operation.queueRequest;

import com.omgservers.schema.model.queueRequest.QueueRequestModel;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

public interface SelectQueueRequestOperation {
    Uni<QueueRequestModel> execute(SqlConnection sqlConnection,
                                   int shard,
                                   Long queueId,
                                   Long id);
}
