package com.omgservers.module.runtime.impl.service.webService.impl.api;

import com.omgservers.model.dto.runtime.DeleteRuntimeCommandRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandsRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeCommandsResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeGrantResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimePermissionRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimePermissionResponse;
import com.omgservers.model.dto.runtime.DeleteRuntimeRequest;
import com.omgservers.model.dto.runtime.DeleteRuntimeResponse;
import com.omgservers.model.dto.runtime.DoBroadcastMessageRequest;
import com.omgservers.model.dto.runtime.DoBroadcastMessageResponse;
import com.omgservers.model.dto.runtime.DoKickClientRequest;
import com.omgservers.model.dto.runtime.DoKickClientResponse;
import com.omgservers.model.dto.runtime.DoMulticastMessageRequest;
import com.omgservers.model.dto.runtime.DoMulticastMessageResponse;
import com.omgservers.model.dto.runtime.DoRespondClientRequest;
import com.omgservers.model.dto.runtime.DoRespondClientResponse;
import com.omgservers.model.dto.runtime.DoSetAttributesRequest;
import com.omgservers.model.dto.runtime.DoSetAttributesResponse;
import com.omgservers.model.dto.runtime.DoSetObjectRequest;
import com.omgservers.model.dto.runtime.DoSetObjectResponse;
import com.omgservers.model.dto.runtime.DoStopRuntimeRequest;
import com.omgservers.model.dto.runtime.DoStopRuntimeResponse;
import com.omgservers.model.dto.runtime.DoUnicastMessageRequest;
import com.omgservers.model.dto.runtime.DoUnicastMessageResponse;
import com.omgservers.model.dto.runtime.FindRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.FindRuntimeGrantResponse;
import com.omgservers.model.dto.runtime.FindRuntimePermissionRequest;
import com.omgservers.model.dto.runtime.FindRuntimePermissionResponse;
import com.omgservers.model.dto.runtime.GetRuntimeRequest;
import com.omgservers.model.dto.runtime.GetRuntimeResponse;
import com.omgservers.model.dto.runtime.HandleRuntimeCommandsRequest;
import com.omgservers.model.dto.runtime.HandleRuntimeCommandsResponse;
import com.omgservers.model.dto.runtime.SyncRuntimeCommandRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeCommandResponse;
import com.omgservers.model.dto.runtime.SyncRuntimeGrantRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeGrantResponse;
import com.omgservers.model.dto.runtime.SyncRuntimePermissionRequest;
import com.omgservers.model.dto.runtime.SyncRuntimePermissionResponse;
import com.omgservers.model.dto.runtime.SyncRuntimeRequest;
import com.omgservers.model.dto.runtime.SyncRuntimeResponse;
import com.omgservers.model.dto.runtime.ViewRuntimeCommandsRequest;
import com.omgservers.model.dto.runtime.ViewRuntimeCommandsResponse;
import com.omgservers.model.internalRole.InternalRoleEnum;
import com.omgservers.module.runtime.impl.service.webService.WebService;
import com.omgservers.operation.handleApiRequest.HandleApiRequestOperation;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RuntimeApiImpl implements RuntimeApi {

    final HandleApiRequestOperation handleApiRequestOperation;
    final WebService webService;

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<SyncRuntimeResponse> syncRuntime(final SyncRuntimeRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::syncRuntime);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<GetRuntimeResponse> getRuntime(final GetRuntimeRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::getRuntime);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DeleteRuntimeResponse> deleteRuntime(final DeleteRuntimeRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::deleteRuntime);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<SyncRuntimePermissionResponse> syncRuntimePermission(final SyncRuntimePermissionRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::syncRuntimePermission);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<FindRuntimePermissionResponse> findRuntimePermission(final FindRuntimePermissionRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::findRuntimePermission);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DeleteRuntimePermissionResponse> deleteRuntimePermission(final DeleteRuntimePermissionRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::deleteRuntimePermission);
    }

    @Override
    public Uni<ViewRuntimeCommandsResponse> viewRuntimeCommands(final ViewRuntimeCommandsRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::viewRuntimeCommands);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<SyncRuntimeCommandResponse> syncRuntimeCommand(final SyncRuntimeCommandRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::syncRuntimeCommand);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<HandleRuntimeCommandsResponse> handleRuntimeCommands(final HandleRuntimeCommandsRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::handleRuntimeCommands);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DeleteRuntimeCommandResponse> deleteRuntimeCommand(final DeleteRuntimeCommandRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::deleteRuntimeCommand);
    }

    @Override
    public Uni<DeleteRuntimeCommandsResponse> deleteRuntimeCommands(final DeleteRuntimeCommandsRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::deleteRuntimeCommands);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<SyncRuntimeGrantResponse> syncRuntimeGrant(final SyncRuntimeGrantRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::syncRuntimeGrant);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<FindRuntimeGrantResponse> findRuntimeGrant(final FindRuntimeGrantRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::findRuntimeGrant);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DeleteRuntimeGrantResponse> deleteRuntimeGrant(final DeleteRuntimeGrantRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::deleteRuntimeGrant);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoRespondClientResponse> doRespondClient(DoRespondClientRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doRespondClient);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoSetAttributesResponse> doSetAttributes(DoSetAttributesRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doSetAttributes);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoSetObjectResponse> doSetObject(DoSetObjectRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doSetObject);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoKickClientResponse> doKickClient(final DoKickClientRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doKickClient);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoStopRuntimeResponse> doStopRuntime(final DoStopRuntimeRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doStopRuntime);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoUnicastMessageResponse> doUnicastMessage(final DoUnicastMessageRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doUnicastMessage);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoMulticastMessageResponse> doMulticastMessage(final DoMulticastMessageRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doMulticastMessage);
    }

    @Override
    @RolesAllowed({InternalRoleEnum.Names.SERVICE})
    public Uni<DoBroadcastMessageResponse> doBroadcastMessage(final DoBroadcastMessageRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::doBroadcastMessage);
    }
}
