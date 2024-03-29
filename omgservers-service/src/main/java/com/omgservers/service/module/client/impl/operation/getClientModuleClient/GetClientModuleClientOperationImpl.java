package com.omgservers.service.module.client.impl.operation.getClientModuleClient;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ApplicationScoped
class GetClientModuleClientOperationImpl implements GetClientModuleClientOperation {

    final Map<URI, ClientModuleClient> cache;

    GetClientModuleClientOperationImpl() {
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized ClientModuleClient getClient(final URI uri) {
        if (!cache.containsKey(uri)) {
            ClientModuleClient restClient = RestClientBuilder.newBuilder()
                    .baseUri(uri)
                    .build(ClientModuleClient.class);
            log.info("Module client was created, uri={}", uri);

            cache.put(uri, restClient);
        }

        return cache.get(uri);
    }

    @Override
    public Boolean hasCacheFor(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri is null");
        }

        return cache.containsKey(uri);
    }

    @Override
    public Integer sizeOfCache() {
        return cache.size();
    }
}
