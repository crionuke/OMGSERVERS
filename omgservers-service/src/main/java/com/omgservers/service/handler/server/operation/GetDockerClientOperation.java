package com.omgservers.service.handler.server.operation;

import com.github.dockerjava.api.DockerClient;

import java.net.URI;

public interface GetDockerClientOperation {
    DockerClient getClient(URI uri);

    Boolean hasCacheFor(URI uri);

    Integer sizeOfCache();
}
