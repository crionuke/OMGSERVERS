package com.omgservers.service.module.runtime.impl.service.runtimeService.impl;

import com.omgservers.model.dto.runtime.CountRuntimeAssignmentsRequest;
import com.omgservers.model.dto.runtime.CountRuntimeAssignmentsResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeAssignmentRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeAssignmentResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandsRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandsResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimePermissionRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimePermissionResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeResponse;
import com.omgservers.model.dto.runtime.FindRuntimeAssignmentRequest;
import com.omgservers.model.dto.runtime.FindRuntimeAssignmentResponse;
import com.omgservers.model.dto.runtime.FindRuntimePermissionRequest;
import com.omgservers.model.dto.runtime.FindRuntimePermissionResponse;
import com.omgservers.model.dto.runtime.GetRuntimeAssignmentRequest;
import com.omgservers.model.dto.runtime.GetRuntimeAssignmentResponse;
import com.omgservers.model.dto.runtime.GetRuntimeRequest;
import com.omgservers.model.dto.runtime.GetRuntimeResponse;
import com.omgservers.model.dto.runtime.InterchangeRequest;
import com.omgservers.model.dto.runtime.InterchangeResponse;
import com.omgservers.model.dto.runtime.SyncClientCommandRequest;
import com.omgservers.model.dto.runtime.SyncClientCommandResponse;
import com.omgservers.model.dto.runtime.SyncRuntimeAssignmentRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeAssignmentResponse;
import com.omgservers.model.dto.runtime.SyncRuntimeCommandRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeCommandResponse;
import com.omgservers.model.dto.runtime.SyncRuntimePermissionRequest;
import com.omgservers.model.dto.runtime.SyncRuntimePermissionResponse;
import com.omgservers.model.dto.runtime.SyncRuntimeRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeResponse;
import com.omgservers.model.dto.runtime.ViewRuntimeAssignmentsRequest;
import com.omgservers.model.dto.runtime.ViewRuntimeAssignmentsResponse;
import com.omgservers.model.dto.runtime.ViewRuntimeCommandsRequest;
import com.omgservers.model.dto.runtime.ViewRuntimeCommandsResponse;
import com.omgservers.model.dto.runtime.ViewRuntimePermissionsRequest;
import com.omgservers.model.dto.runtime.ViewRuntimePermissionsResponse;
import com.omgservers.service.module.runtime.impl.operation.getRuntimeModuleClient.GetRuntimeModuleClientOperation;
import com.omgservers.service.module.runtime.impl.operation.getRuntimeModuleClient.RuntimeModuleClient;
import com.omgservers.service.module.runtime.impl.service.runtimeService.RuntimeService;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.countRuntimeAssignments.CountRuntimeAssignmentsMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.deleteRuntime.DeleteRuntimeMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.deleteRuntimeAssignment.DeleteRuntimeAssignmentMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.deleteRuntimeCommand.DeleteRuntimeCommandMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.deleteRuntimeCommands.DeleteRuntimeCommandsMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.deleteRuntimePermission.DeleteRuntimePermissionMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.findRuntimeAssignment.FindRuntimeAssignmentMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.findRuntimePermission.FindRuntimePermissionMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.getRuntime.GetRuntimeMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.getRuntimeAssignment.GetRuntimeAssignmentMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.interchange.InterchangeMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.syncClientCommand.SyncClientCommandMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.syncRuntime.SyncRuntimeMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.syncRuntimeAssignment.SyncRuntimeAssignmentMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.syncRuntimeCommand.SyncRuntimeCommandMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.syncRuntimePermission.SyncRuntimePermissionMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.viewRuntimeAssignments.ViewRuntimeAssignmentsMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.viewRuntimeCommands.ViewRuntimeCommandsMethod;
import com.omgservers.service.module.runtime.impl.service.runtimeService.impl.method.viewRuntimePermissions.ViewRuntimePermissionsMethod;
import com.omgservers.service.operation.handleInternalRequest.HandleInternalRequestOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RuntimeServiceImpl implements RuntimeService {

    final DeleteRuntimePermissionMethod deleteRuntimePermissionMethod;
    final ViewRuntimePermissionsMethod viewRuntimePermissionsMethod;
    final SyncRuntimePermissionMethod syncRuntimePermissionMethod;
    final FindRuntimePermissionMethod findRuntimePermissionMethod;
    final DeleteRuntimeCommandsMethod deleteRuntimeCommandsMethod;
    final DeleteRuntimeCommandMethod deleteRuntimeCommandMethod;
    final ViewRuntimeCommandsMethod viewRuntimeCommandsMethod;
    final DeleteRuntimeAssignmentMethod deleteRuntimeAssignmentMethod;
    final CountRuntimeAssignmentsMethod countRuntimeAssignmentsMethod;
    final SyncRuntimeCommandMethod syncRuntimeCommandMethod;
    final ViewRuntimeAssignmentsMethod viewRuntimeAssignmentsMethod;
    final SyncClientCommandMethod syncClientCommandMethod;
    final SyncRuntimeAssignmentMethod syncRuntimeAssignmentMethod;
    final FindRuntimeAssignmentMethod findRuntimeAssignmentMethod;
    final GetRuntimeAssignmentMethod getRuntimeAssignmentMethod;
    final DeleteRuntimeMethod deleteRuntimeMethod;
    final SyncRuntimeMethod syncRuntimeMethod;
    final InterchangeMethod interchangeMethod;
    final GetRuntimeMethod getRuntimeMethod;

    final GetRuntimeModuleClientOperation getRuntimeModuleClientOperation;
    final HandleInternalRequestOperation handleInternalRequestOperation;

    @Override
    public Uni<SyncRuntimeResponse> syncRuntime(@Valid final SyncRuntimeRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncLobbyRuntime,
                syncRuntimeMethod::syncRuntime);
    }

    @Override
    public Uni<GetRuntimeResponse> getRuntime(@Valid final GetRuntimeRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::getRuntime,
                getRuntimeMethod::getRuntime);
    }

    @Override
    public Uni<DeleteRuntimeResponse> deleteRuntime(@Valid final DeleteRuntimeRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteRuntime,
                deleteRuntimeMethod::deleteRuntime);
    }

    @Override
    public Uni<SyncRuntimePermissionResponse> syncRuntimePermission(@Valid final SyncRuntimePermissionRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncRuntimePermission,
                syncRuntimePermissionMethod::syncRuntimePermission);
    }

    @Override
    public Uni<ViewRuntimePermissionsResponse> viewRuntimePermissions(
            @Valid final ViewRuntimePermissionsRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::viewRuntimePermissions,
                viewRuntimePermissionsMethod::viewRuntimePermissions);
    }

    @Override
    public Uni<FindRuntimePermissionResponse> findRuntimePermission(@Valid final FindRuntimePermissionRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::findRuntimePermission,
                findRuntimePermissionMethod::findRuntimePermission);
    }

    @Override
    public Uni<DeleteRuntimePermissionResponse> deleteRuntimePermission(
            @Valid final DeleteRuntimePermissionRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteRuntimePermission,
                deleteRuntimePermissionMethod::deleteRuntimePermission);
    }

    @Override
    public Uni<ViewRuntimeCommandsResponse> viewRuntimeCommands(@Valid final ViewRuntimeCommandsRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::viewRuntimeCommands,
                viewRuntimeCommandsMethod::viewRuntimeCommands);
    }

    @Override
    public Uni<SyncRuntimeCommandResponse> syncRuntimeCommand(@Valid final SyncRuntimeCommandRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncRuntimeCommand,
                syncRuntimeCommandMethod::syncRuntimeCommand);
    }

    @Override
    public Uni<SyncClientCommandResponse> syncClientCommand(@Valid final SyncClientCommandRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncClientCommand,
                syncClientCommandMethod::syncClientCommand);
    }

    @Override
    public Uni<DeleteRuntimeCommandResponse> deleteRuntimeCommand(@Valid final DeleteRuntimeCommandRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteRuntimeCommand,
                deleteRuntimeCommandMethod::deleteRuntimeCommand);
    }

    @Override
    public Uni<DeleteRuntimeCommandsResponse> deleteRuntimeCommands(@Valid final DeleteRuntimeCommandsRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteRuntimeCommands,
                deleteRuntimeCommandsMethod::deleteRuntimeCommands);
    }

    @Override
    public Uni<GetRuntimeAssignmentResponse> getRuntimeAssignment(@Valid final GetRuntimeAssignmentRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::getRuntimeAssignment,
                getRuntimeAssignmentMethod::getRuntimeAssignment);
    }

    @Override
    public Uni<FindRuntimeAssignmentResponse> findRuntimeAssignment(@Valid final FindRuntimeAssignmentRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::findRuntimeAssignment,
                findRuntimeAssignmentMethod::findRuntimeAssignment);
    }

    @Override
    public Uni<ViewRuntimeAssignmentsResponse> viewRuntimeAssignments(@Valid final ViewRuntimeAssignmentsRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::viewRuntimeAssignments,
                viewRuntimeAssignmentsMethod::viewRuntimeAssignments);
    }

    @Override
    public Uni<CountRuntimeAssignmentsResponse> countRuntimeAssignments(@Valid final CountRuntimeAssignmentsRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::countRuntimeAssignments,
                countRuntimeAssignmentsMethod::countRuntimeAssignments);
    }

    @Override
    public Uni<SyncRuntimeAssignmentResponse> syncRuntimeAssignment(@Valid final SyncRuntimeAssignmentRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::syncRuntimeAssignment,
                syncRuntimeAssignmentMethod::syncRuntimeAssignment);
    }

    @Override
    public Uni<DeleteRuntimeAssignmentResponse> deleteRuntimeAssignment(@Valid final DeleteRuntimeAssignmentRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::deleteRuntimeAssignment,
                deleteRuntimeAssignmentMethod::deleteRuntimeAssignment);
    }

    @Override
    public Uni<InterchangeResponse> interchange(@Valid final InterchangeRequest request) {
        return handleInternalRequestOperation.handleInternalRequest(log, request,
                getRuntimeModuleClientOperation::getClient,
                RuntimeModuleClient::interchange,
                interchangeMethod::interchange);
    }
}
