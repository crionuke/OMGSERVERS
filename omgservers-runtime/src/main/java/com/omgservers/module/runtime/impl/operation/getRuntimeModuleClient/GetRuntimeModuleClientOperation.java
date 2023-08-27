package com.omgservers.module.runtime.impl.operation.getRuntimeModuleClient;

import java.net.URI;

public interface GetRuntimeModuleClientOperation {
    RuntimeModuleClient getClient(URI uri);

    Boolean hasCacheFor(URI uri);

    Integer sizeOfCache();
}
