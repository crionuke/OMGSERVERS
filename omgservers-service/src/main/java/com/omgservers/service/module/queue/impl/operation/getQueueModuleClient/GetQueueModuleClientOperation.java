package com.omgservers.service.module.queue.impl.operation.getQueueModuleClient;

import java.net.URI;

public interface GetQueueModuleClientOperation {
    QueueModuleClient getClient(URI uri);

    Boolean hasCacheFor(URI uri);

    Integer sizeOfCache();
}
