package com.omgservers.module.runtime.impl.service.runtimeShardedService.impl;

import com.omgservers.module.runtime.impl.operation.getRuntimeModuleClient.GetRuntimeModuleClientOperation;
import com.omgservers.module.runtime.impl.operation.getRuntimeModuleClient.RuntimeModuleClient;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.RuntimeShardedService;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.deleteCommand.DeleteCommandMethod;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.deleteRuntime.DeleteRuntimeMethod;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.doUpdate.DoUpdateMethod;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.getRuntime.GetRuntimeMethod;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncCommand.SyncCommandMethod;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncRuntime.SyncRuntimeMethod;
import com.omgservers.operation.handleInternalRequest.HandleInternalRequestOperation;
import com.omgservers.dto.runtime.DeleteRuntimeCommandShardedRequest;
import com.omgservers.dto.runtime.DeleteRuntimeCommandShardedResponse;
import com.omgservers.dto.runtime.DeleteRuntimeShardedRequest;
import com.omgservers.dto.runtime.DeleteRuntimeShardedResponse;
import com.omgservers.dto.runtime.DoUpdateShardedRequest;
import com.omgservers.dto.runtime.GetRuntimeShardedRequest;
import com.omgservers.dto.runtime.GetRuntimeShardedResponse;
import com.omgservers.dto.runtime.SyncRuntimeCommandShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeCommandShardedResponse;
import com.omgservers.dto.runtime.SyncRuntimeShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeShardedResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RuntimeShardedServiceImpl implements RuntimeShardedService {

    final GetRuntimeModuleClientOperation getRuntimeModuleClientOperation;
    final HandleInternalRequestOperation handleInternalRequestOperation;

    final DeleteRuntimeMethod deleteRuntimeMethod;
    final DeleteCommandMethod deleteCommandMethod;
    final SyncRuntimeMethod syncRuntimeMethod;
    final SyncCommandMethod syncCommandMethod;
    final GetRuntimeMethod getRuntimeMethod;
    final DoUpdateMethod doUpdateMethod;

    @Override
    public Uni<SyncRuntimeShardedResponse> syncRuntime(SyncRuntimeShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                SyncRuntimeShardedRequest::validate,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncRuntime,
                syncRuntimeMethod::syncRuntime);
    }

    @Override
    public Uni<GetRuntimeShardedResponse> getRuntime(GetRuntimeShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                GetRuntimeShardedRequest::validate,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::getRuntime,
                getRuntimeMethod::getRuntime);
    }

    @Override
    public Uni<DeleteRuntimeShardedResponse> deleteRuntime(DeleteRuntimeShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DeleteRuntimeShardedRequest::validate,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteRuntime,
                deleteRuntimeMethod::deleteRuntime);
    }

    @Override
    public Uni<SyncRuntimeCommandShardedResponse> syncCommand(SyncRuntimeCommandShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                SyncRuntimeCommandShardedRequest::validate,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncCommand,
                syncCommandMethod::syncCommand);
    }

    @Override
    public Uni<DeleteRuntimeCommandShardedResponse> deleteCommand(DeleteRuntimeCommandShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DeleteRuntimeCommandShardedRequest::validate,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteCommand,
                deleteCommandMethod::deleteCommand);
    }

    @Override
    public Uni<Void> doUpdate(DoUpdateShardedRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                DoUpdateShardedRequest::validate,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::doUpdate,
                doUpdateMethod::doUpdate);
    }
}
