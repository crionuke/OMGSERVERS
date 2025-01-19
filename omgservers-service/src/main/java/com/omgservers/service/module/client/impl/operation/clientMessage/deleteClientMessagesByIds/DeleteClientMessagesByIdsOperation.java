package com.omgservers.service.module.client.impl.operation.clientMessage.deleteClientMessagesByIds;

import com.omgservers.service.operation.server.ChangeContext;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;

import java.util.List;

public interface DeleteClientMessagesByIdsOperation {
    Uni<Boolean> deleteClientMessagesByIds(ChangeContext<?> changeContext,
                                           SqlConnection sqlConnection,
                                           int shard,
                                           Long clientId,
                                           List<Long> ids);
}
