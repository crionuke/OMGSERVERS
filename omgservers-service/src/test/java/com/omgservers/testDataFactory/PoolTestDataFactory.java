package com.omgservers.testDataFactory;

import com.omgservers.schema.model.pool.PoolModel;
import com.omgservers.schema.model.poolRequest.PoolRequestConfigDto;
import com.omgservers.schema.model.poolRequest.PoolRequestModel;
import com.omgservers.schema.model.poolServer.PoolServerConfigModel;
import com.omgservers.schema.model.poolServer.PoolServerModel;
import com.omgservers.schema.model.poolServer.PoolServerQualifierEnum;
import com.omgservers.schema.model.poolSeverContainer.PoolServerContainerConfigDto;
import com.omgservers.schema.model.poolSeverContainer.PoolServerContainerModel;
import com.omgservers.schema.model.runtime.RuntimeModel;
import com.omgservers.schema.module.pool.pool.GetPoolRequest;
import com.omgservers.schema.module.pool.pool.SyncPoolRequest;
import com.omgservers.schema.module.pool.poolRequest.SyncPoolRequestRequest;
import com.omgservers.schema.module.pool.poolServer.SyncPoolServerRequest;
import com.omgservers.schema.module.pool.poolServerContainer.SyncPoolServerContainerRequest;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.factory.pool.PoolModelFactory;
import com.omgservers.service.factory.pool.PoolRequestModelFactory;
import com.omgservers.service.factory.pool.PoolServerContainerModelFactory;
import com.omgservers.service.factory.pool.PoolServerModelFactory;
import com.omgservers.service.module.pool.impl.service.poolService.testInterface.PoolServiceTestInterface;
import com.omgservers.service.operation.getConfig.GetConfigOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.HashMap;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class PoolTestDataFactory {

    final PoolServiceTestInterface poolService;

    final GetConfigOperation getConfigOperation;

    final PoolServerContainerModelFactory poolServerContainerModelFactory;
    final PoolRequestModelFactory poolRequestModelFactory;
    final PoolServerModelFactory poolServerModelFactory;
    final PoolModelFactory poolModelFactory;

    public PoolModel createDefaultPool() {
        final var defaultPoolId = getConfigOperation.getServiceConfig().defaults().poolId();

        try {
            final var getPoolRequest = new GetPoolRequest(defaultPoolId);
            log.info("Default pool was already created, defaultPoolId={}", defaultPoolId);
            return poolService.getPool(getPoolRequest).getPool();
        } catch (ServerSideNotFoundException e) {
            final var pool = poolModelFactory.create(defaultPoolId);
            final var syncPoolRequest = new SyncPoolRequest(pool);
            poolService.syncPool(syncPoolRequest);
            return pool;
        }
    }

    public PoolModel createPool() {
        final var pool = poolModelFactory.create();
        final var syncPoolRequest = new SyncPoolRequest(pool);
        poolService.syncPool(syncPoolRequest);
        return pool;
    }

    public PoolRequestModel createPoolRequest(final PoolModel pool, final RuntimeModel runtime) {
        final var poolId = pool.getId();
        final var runtimeId = runtime.getId();
        final var runtimeQualifier = runtime.getQualifier();
        final var config = PoolRequestConfigDto.create();

        final var poolRequest = poolRequestModelFactory.create(poolId,
                runtimeId,
                runtimeQualifier,
                config);
        final var syncPoolRequestRequest = new SyncPoolRequestRequest(poolRequest);
        poolService.syncPoolRequest(syncPoolRequestRequest);
        return poolRequest;
    }

    public PoolServerModel createPoolServer(final PoolModel pool) {
        final var poolId = pool.getId();
        final var config = PoolServerConfigModel.create();
        config.setDockerHostConfig(PoolServerConfigModel.DockerHostConfig.builder()
                .dockerDaemonUri(URI.create("unix:///var/run/docker.sock"))
                .cpuCount(1000)
                .memorySize(2048)
                .maxContainers(16)
                .build());

        final var poolServer = poolServerModelFactory.create(poolId,
                PoolServerQualifierEnum.DOCKER_HOST, config);
        final var syncPoolServerRequest = new SyncPoolServerRequest(poolServer);
        poolService.syncPoolServer(syncPoolServerRequest);
        return poolServer;
    }

    public PoolServerContainerModel createPoolServerContainer(final PoolServerModel poolServer,
                                                              final RuntimeModel runtime) {
        final var poolId = poolServer.getPoolId();
        final var serverId = poolServer.getId();
        final var runtimeId = runtime.getId();
        final var runtimeQualifier = runtime.getQualifier();

        final var dockerImage = "ubuntu:latest";
        final var config = PoolServerContainerConfigDto.create();
        config.setImageId(dockerImage);
        config.setCpuLimitInMilliseconds(100);
        config.setMemoryLimitInMegabytes(200);
        config.setEnvironment(new HashMap<>());

        final var poolServerContainer = poolServerContainerModelFactory.create(poolId,
                serverId,
                runtimeId,
                runtimeQualifier,
                config);
        final var syncPoolServerContainerRequest = new SyncPoolServerContainerRequest(poolServerContainer);
        poolService.syncPoolServerContainer(syncPoolServerContainerRequest);
        return poolServerContainer;
    }
}
