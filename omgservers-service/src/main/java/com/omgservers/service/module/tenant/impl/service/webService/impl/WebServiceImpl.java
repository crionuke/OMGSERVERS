package com.omgservers.service.module.tenant.impl.service.webService.impl;

import com.omgservers.schema.module.tenant.tenant.DeleteTenantRequest;
import com.omgservers.schema.module.tenant.tenant.DeleteTenantResponse;
import com.omgservers.schema.module.tenant.tenant.GetTenantDataRequest;
import com.omgservers.schema.module.tenant.tenant.GetTenantDataResponse;
import com.omgservers.schema.module.tenant.tenant.GetTenantRequest;
import com.omgservers.schema.module.tenant.tenant.GetTenantResponse;
import com.omgservers.schema.module.tenant.tenant.SyncTenantRequest;
import com.omgservers.schema.module.tenant.tenant.SyncTenantResponse;
import com.omgservers.schema.module.tenant.tenantDeployment.DeleteTenantDeploymentRequest;
import com.omgservers.schema.module.tenant.tenantDeployment.DeleteTenantDeploymentResponse;
import com.omgservers.schema.module.tenant.tenantDeployment.GetTenantDeploymentDataRequest;
import com.omgservers.schema.module.tenant.tenantDeployment.GetTenantDeploymentDataResponse;
import com.omgservers.schema.module.tenant.tenantDeployment.GetTenantDeploymentRequest;
import com.omgservers.schema.module.tenant.tenantDeployment.GetTenantDeploymentResponse;
import com.omgservers.schema.module.tenant.tenantDeployment.SelectTenantDeploymentRequest;
import com.omgservers.schema.module.tenant.tenantDeployment.SelectTenantDeploymentResponse;
import com.omgservers.schema.module.tenant.tenantDeployment.SyncTenantDeploymentRequest;
import com.omgservers.schema.module.tenant.tenantDeployment.SyncTenantDeploymentResponse;
import com.omgservers.schema.module.tenant.tenantDeployment.ViewTenantDeploymentsRequest;
import com.omgservers.schema.module.tenant.tenantDeployment.ViewTenantDeploymentsResponse;
import com.omgservers.schema.module.tenant.tenantImageRef.DeleteTenantImageRefRequest;
import com.omgservers.schema.module.tenant.tenantImageRef.DeleteTenantImageRefResponse;
import com.omgservers.schema.module.tenant.tenantImageRef.FindTenantImageRefRequest;
import com.omgservers.schema.module.tenant.tenantImageRef.FindTenantImageRefResponse;
import com.omgservers.schema.module.tenant.tenantImageRef.GetTenantImageRefRequest;
import com.omgservers.schema.module.tenant.tenantImageRef.GetTenantImageRefResponse;
import com.omgservers.schema.module.tenant.tenantImageRef.SyncTenantImageRefRequest;
import com.omgservers.schema.module.tenant.tenantImageRef.SyncTenantImageRefResponse;
import com.omgservers.schema.module.tenant.tenantImageRef.ViewTenantImageRefsRequest;
import com.omgservers.schema.module.tenant.tenantImageRef.ViewTenantImageRefsResponse;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.DeleteTenantJenkinsRequestRequest;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.DeleteTenantJenkinsRequestResponse;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.GetTenantJenkinsRequestRequest;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.GetTenantJenkinsRequestResponse;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.SyncTenantJenkinsRequestRequest;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.SyncTenantJenkinsRequestResponse;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.ViewTenantJenkinsRequestsRequest;
import com.omgservers.schema.module.tenant.tenantJenkinsRequest.ViewTenantJenkinsRequestsResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRef.DeleteTenantLobbyRefRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRef.DeleteTenantLobbyRefResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRef.FindTenantLobbyRefRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRef.FindTenantLobbyRefResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRef.GetTenantLobbyRefRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRef.GetTenantLobbyRefResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRef.SyncTenantLobbyRefRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRef.SyncTenantLobbyRefResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRef.ViewTenantLobbyRefsRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRef.ViewTenantLobbyRefsResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.DeleteTenantLobbyRequestRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.DeleteTenantLobbyRequestResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.FindTenantLobbyRequestRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.FindTenantLobbyRequestResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.GetTenantLobbyRequestRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.GetTenantLobbyRequestResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.SyncTenantLobbyRequestRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.SyncTenantLobbyRequestResponse;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.ViewTenantLobbyRequestsRequest;
import com.omgservers.schema.module.tenant.tenantLobbyRequest.ViewTenantLobbyRequestsResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.DeleteTenantMatchmakerRefRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.DeleteTenantMatchmakerRefResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.FindTenantMatchmakerRefRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.FindTenantMatchmakerRefResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.GetTenantMatchmakerRefRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.GetTenantMatchmakerRefResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.SyncTenantMatchmakerRefRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.SyncTenantMatchmakerRefResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.ViewTenantMatchmakerRefsRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRef.ViewTenantMatchmakerRefsResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.DeleteTenantMatchmakerRequestRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.DeleteTenantMatchmakerRequestResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.FindTenantMatchmakerRequestRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.FindTenantMatchmakerRequestResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.GetTenantMatchmakerRequestRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.GetTenantMatchmakerRequestResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.SyncTenantMatchmakerRequestRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.SyncTenantMatchmakerRequestResponse;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.ViewTenantMatchmakerRequestsRequest;
import com.omgservers.schema.module.tenant.tenantMatchmakerRequest.ViewTenantMatchmakerRequestsResponse;
import com.omgservers.schema.module.tenant.tenantPermission.DeleteTenantPermissionRequest;
import com.omgservers.schema.module.tenant.tenantPermission.DeleteTenantPermissionResponse;
import com.omgservers.schema.module.tenant.tenantPermission.SyncTenantPermissionRequest;
import com.omgservers.schema.module.tenant.tenantPermission.SyncTenantPermissionResponse;
import com.omgservers.schema.module.tenant.tenantPermission.VerifyTenantPermissionExistsRequest;
import com.omgservers.schema.module.tenant.tenantPermission.VerifyTenantPermissionExistsResponse;
import com.omgservers.schema.module.tenant.tenantPermission.ViewTenantPermissionsRequest;
import com.omgservers.schema.module.tenant.tenantPermission.ViewTenantPermissionsResponse;
import com.omgservers.schema.module.tenant.tenantProject.DeleteTenantProjectRequest;
import com.omgservers.schema.module.tenant.tenantProject.DeleteTenantProjectResponse;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectDataRequest;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectDataResponse;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectRequest;
import com.omgservers.schema.module.tenant.tenantProject.GetTenantProjectResponse;
import com.omgservers.schema.module.tenant.tenantProject.SyncTenantProjectRequest;
import com.omgservers.schema.module.tenant.tenantProject.SyncTenantProjectResponse;
import com.omgservers.schema.module.tenant.tenantProject.ViewTenantProjectsRequest;
import com.omgservers.schema.module.tenant.tenantProject.ViewTenantProjectsResponse;
import com.omgservers.schema.module.tenant.tenantProjectPermission.DeleteTenantProjectPermissionRequest;
import com.omgservers.schema.module.tenant.tenantProjectPermission.DeleteTenantProjectPermissionResponse;
import com.omgservers.schema.module.tenant.tenantProjectPermission.SyncTenantProjectPermissionRequest;
import com.omgservers.schema.module.tenant.tenantProjectPermission.SyncTenantProjectPermissionResponse;
import com.omgservers.schema.module.tenant.tenantProjectPermission.VerifyTenantProjectPermissionExistsRequest;
import com.omgservers.schema.module.tenant.tenantProjectPermission.VerifyTenantProjectPermissionExistsResponse;
import com.omgservers.schema.module.tenant.tenantProjectPermission.ViewTenantProjectPermissionsRequest;
import com.omgservers.schema.module.tenant.tenantProjectPermission.ViewTenantProjectPermissionsResponse;
import com.omgservers.schema.module.tenant.tenantStage.DeleteTenantStageRequest;
import com.omgservers.schema.module.tenant.tenantStage.DeleteTenantStageResponse;
import com.omgservers.schema.module.tenant.tenantStage.GetTenantStageDataRequest;
import com.omgservers.schema.module.tenant.tenantStage.GetTenantStageDataResponse;
import com.omgservers.schema.module.tenant.tenantStage.GetTenantStageRequest;
import com.omgservers.schema.module.tenant.tenantStage.GetTenantStageResponse;
import com.omgservers.schema.module.tenant.tenantStage.SyncTenantStageRequest;
import com.omgservers.schema.module.tenant.tenantStage.SyncTenantStageResponse;
import com.omgservers.schema.module.tenant.tenantStage.ViewTenantStagesRequest;
import com.omgservers.schema.module.tenant.tenantStage.ViewTenantStagesResponse;
import com.omgservers.schema.module.tenant.tenantStagePermission.DeleteTenantStagePermissionRequest;
import com.omgservers.schema.module.tenant.tenantStagePermission.DeleteTenantStagePermissionResponse;
import com.omgservers.schema.module.tenant.tenantStagePermission.SyncTenantStagePermissionRequest;
import com.omgservers.schema.module.tenant.tenantStagePermission.SyncTenantStagePermissionResponse;
import com.omgservers.schema.module.tenant.tenantStagePermission.VerifyTenantStagePermissionExistsRequest;
import com.omgservers.schema.module.tenant.tenantStagePermission.VerifyTenantStagePermissionExistsResponse;
import com.omgservers.schema.module.tenant.tenantStagePermission.ViewTenantStagePermissionsRequest;
import com.omgservers.schema.module.tenant.tenantStagePermission.ViewTenantStagePermissionsResponse;
import com.omgservers.schema.module.tenant.tenantVersion.DeleteTenantVersionRequest;
import com.omgservers.schema.module.tenant.tenantVersion.DeleteTenantVersionResponse;
import com.omgservers.schema.module.tenant.tenantVersion.GetTenantVersionConfigRequest;
import com.omgservers.schema.module.tenant.tenantVersion.GetTenantVersionConfigResponse;
import com.omgservers.schema.module.tenant.tenantVersion.GetTenantVersionDataRequest;
import com.omgservers.schema.module.tenant.tenantVersion.GetTenantVersionDataResponse;
import com.omgservers.schema.module.tenant.tenantVersion.GetTenantVersionRequest;
import com.omgservers.schema.module.tenant.tenantVersion.GetTenantVersionResponse;
import com.omgservers.schema.module.tenant.tenantVersion.SyncTenantVersionRequest;
import com.omgservers.schema.module.tenant.tenantVersion.SyncTenantVersionResponse;
import com.omgservers.schema.module.tenant.tenantVersion.ViewTenantVersionsRequest;
import com.omgservers.schema.module.tenant.tenantVersion.ViewTenantVersionsResponse;
import com.omgservers.service.module.tenant.impl.service.tenantService.TenantService;
import com.omgservers.service.module.tenant.impl.service.webService.WebService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class WebServiceImpl implements WebService {

    final TenantService tenantService;

    /*
    Tenant
     */

    @Override
    public Uni<GetTenantResponse> getTenant(final GetTenantRequest request) {
        return tenantService.getTenant(request);
    }

    @Override
    public Uni<GetTenantDataResponse> getTenantData(final GetTenantDataRequest request) {
        return tenantService.getTenantData(request);
    }

    @Override
    public Uni<SyncTenantResponse> syncTenant(final SyncTenantRequest request) {
        return tenantService.syncTenant(request);
    }

    @Override
    public Uni<DeleteTenantResponse> deleteTenant(final DeleteTenantRequest request) {
        return tenantService.deleteTenant(request);
    }

    /*
    TenantPermission
     */

    @Override
    public Uni<ViewTenantPermissionsResponse> viewTenantPermissions(final ViewTenantPermissionsRequest request) {
        return tenantService.viewTenantPermissions(request);
    }

    @Override
    public Uni<VerifyTenantPermissionExistsResponse> verifyTenantPermissionExists(
            final VerifyTenantPermissionExistsRequest request) {
        return tenantService.verifyTenantPermissionExists(request);
    }

    @Override
    public Uni<SyncTenantPermissionResponse> syncTenantPermission(final SyncTenantPermissionRequest request) {
        return tenantService.syncTenantPermission(request);
    }

    @Override
    public Uni<DeleteTenantPermissionResponse> deleteTenantPermission(final DeleteTenantPermissionRequest request) {
        return tenantService.deleteTenantPermission(request);
    }

    /*
    TenantProject
     */

    @Override
    public Uni<GetTenantProjectResponse> getTenantProject(final GetTenantProjectRequest request) {
        return tenantService.getTenantProject(request);
    }

    @Override
    public Uni<GetTenantProjectDataResponse> getTenantProjectData(final GetTenantProjectDataRequest request) {
        return tenantService.getTenantProjectData(request);
    }

    @Override
    public Uni<ViewTenantProjectsResponse> viewTenantProjects(final ViewTenantProjectsRequest request) {
        return tenantService.viewTenantProjects(request);
    }

    @Override
    public Uni<SyncTenantProjectResponse> syncTenantProject(final SyncTenantProjectRequest request) {
        return tenantService.syncTenantProject(request);
    }

    @Override
    public Uni<DeleteTenantProjectResponse> deleteTenantProject(final DeleteTenantProjectRequest request) {
        return tenantService.deleteTenantProject(request);
    }

    /*
    TenantProjectPermission
     */

    @Override
    public Uni<ViewTenantProjectPermissionsResponse> viewTenantProjectPermissions(
            final ViewTenantProjectPermissionsRequest request) {
        return tenantService.viewTenantProjectPermissions(request);
    }

    @Override
    public Uni<VerifyTenantProjectPermissionExistsResponse> verifyTenantProjectPermissionExists(
            final VerifyTenantProjectPermissionExistsRequest request) {
        return tenantService.verifyTenantProjectPermissionExists(request);
    }

    @Override
    public Uni<SyncTenantProjectPermissionResponse> syncTenantProjectPermission(
            final SyncTenantProjectPermissionRequest request) {
        return tenantService.syncTenantProjectPermission(request);
    }

    @Override
    public Uni<DeleteTenantProjectPermissionResponse> deleteTenantProjectPermission(
            final DeleteTenantProjectPermissionRequest request) {
        return tenantService.deleteTenantProjectPermission(request);
    }

    /*
    TenantStage
     */

    @Override
    public Uni<GetTenantStageResponse> getTenantStage(final GetTenantStageRequest request) {
        return tenantService.getTenantStage(request);
    }

    @Override
    public Uni<GetTenantStageDataResponse> getTenantStageData(final GetTenantStageDataRequest request) {
        return tenantService.getTenantStageData(request);
    }

    @Override
    public Uni<ViewTenantStagesResponse> viewTenantStages(final ViewTenantStagesRequest request) {
        return tenantService.viewTenantStages(request);
    }

    @Override
    public Uni<SyncTenantStageResponse> syncTenantStage(final SyncTenantStageRequest request) {
        return tenantService.syncTenantStage(request);
    }

    @Override
    public Uni<DeleteTenantStageResponse> deleteTenantStage(final DeleteTenantStageRequest request) {
        return tenantService.deleteTenantStage(request);
    }

    /*
    TenantStagePermission
     */

    @Override
    public Uni<ViewTenantStagePermissionsResponse> viewTenantStagePermissions(
            final ViewTenantStagePermissionsRequest request) {
        return tenantService.viewTenantStagePermissions(request);
    }

    @Override
    public Uni<VerifyTenantStagePermissionExistsResponse> verifyTenantStagePermissionExists(
            final VerifyTenantStagePermissionExistsRequest request) {
        return tenantService.verifyTenantStagePermissionExists(request);
    }

    @Override
    public Uni<SyncTenantStagePermissionResponse> syncTenantStagePermission(
            final SyncTenantStagePermissionRequest request) {
        return tenantService.syncTenantStagePermission(request);
    }

    @Override
    public Uni<DeleteTenantStagePermissionResponse> deleteTenantStagePermission(
            final DeleteTenantStagePermissionRequest request) {
        return tenantService.deleteTenantStagePermission(request);
    }

    /*
    TenantVersion
     */

    @Override
    public Uni<GetTenantVersionResponse> getTenantVersion(final GetTenantVersionRequest request) {
        return tenantService.getTenantVersion(request);
    }

    @Override
    public Uni<GetTenantVersionConfigResponse> getTenantVersionConfig(final GetTenantVersionConfigRequest request) {
        return tenantService.getTenantVersionConfig(request);
    }

    @Override
    public Uni<GetTenantVersionDataResponse> getTenantVersionData(final GetTenantVersionDataRequest request) {
        return tenantService.getTenantVersionData(request);
    }

    @Override
    public Uni<ViewTenantVersionsResponse> viewTenantVersions(final ViewTenantVersionsRequest request) {
        return tenantService.viewTenantVersions(request);
    }

    @Override
    public Uni<SyncTenantVersionResponse> syncTenantVersion(final SyncTenantVersionRequest request) {
        return tenantService.syncTenantVersion(request);
    }

    @Override
    public Uni<DeleteTenantVersionResponse> deleteTenantVersion(final DeleteTenantVersionRequest request) {
        return tenantService.deleteTenantVersion(request);
    }

    /*
    TenantJenkinsRequest
     */

    @Override
    public Uni<GetTenantJenkinsRequestResponse> getTenantJenkinsRequest(
            final GetTenantJenkinsRequestRequest request) {
        return tenantService.getTenantJenkinsRequest(request);
    }

    @Override
    public Uni<ViewTenantJenkinsRequestsResponse> viewTenantJenkinsRequests(
            final ViewTenantJenkinsRequestsRequest request) {
        return tenantService.viewTenantJenkinsRequests(request);
    }

    @Override
    public Uni<SyncTenantJenkinsRequestResponse> syncTenantJenkinsRequest(
            final SyncTenantJenkinsRequestRequest request) {
        return tenantService.syncTenantJenkinsRequest(request);
    }

    @Override
    public Uni<DeleteTenantJenkinsRequestResponse> deleteTenantJenkinsRequest(
            final DeleteTenantJenkinsRequestRequest request) {
        return tenantService.deleteTenantJenkinsRequest(request);
    }

    /*
    TenantImageRef
     */

    @Override
    public Uni<GetTenantImageRefResponse> getTenantImageRef(final GetTenantImageRefRequest request) {
        return tenantService.getTenantImageRef(request);
    }

    @Override
    public Uni<FindTenantImageRefResponse> findTenantImageRef(final FindTenantImageRefRequest request) {
        return tenantService.findTenantImageRef(request);
    }

    @Override
    public Uni<ViewTenantImageRefsResponse> viewTenantImageRefs(final ViewTenantImageRefsRequest request) {
        return tenantService.viewTenantImageRefs(request);
    }

    @Override
    public Uni<SyncTenantImageRefResponse> syncTenantImageRef(final SyncTenantImageRefRequest request) {
        return tenantService.syncTenantImageRef(request);
    }

    @Override
    public Uni<DeleteTenantImageRefResponse> deleteTenantImageRef(final DeleteTenantImageRefRequest request) {
        return tenantService.deleteTenantImageRef(request);
    }

    /*
    TenantDeployment
     */

    @Override
    public Uni<GetTenantDeploymentResponse> getTenantDeployment(final GetTenantDeploymentRequest request) {
        return tenantService.getTenantDeployment(request);
    }

    @Override
    public Uni<GetTenantDeploymentDataResponse> getTenantDeploymentData(final GetTenantDeploymentDataRequest request) {
        return tenantService.getTenantDeploymentData(request);
    }

    @Override
    public Uni<SelectTenantDeploymentResponse> selectTenantDeployment(final SelectTenantDeploymentRequest request) {
        return tenantService.selectTenantDeployment(request);
    }

    @Override
    public Uni<ViewTenantDeploymentsResponse> viewTenantDeployments(final ViewTenantDeploymentsRequest request) {
        return tenantService.viewTenantDeployments(request);
    }

    @Override
    public Uni<SyncTenantDeploymentResponse> syncTenantDeployment(final SyncTenantDeploymentRequest request) {
        return tenantService.syncTenantDeployment(request);
    }

    @Override
    public Uni<DeleteTenantDeploymentResponse> deleteTenantDeployment(final DeleteTenantDeploymentRequest request) {
        return tenantService.deleteTenantDeployment(request);
    }

    /*
    TenantLobbyRequest
     */

    @Override
    public Uni<GetTenantLobbyRequestResponse> getTenantLobbyRequest(final GetTenantLobbyRequestRequest request) {
        return tenantService.getTenantLobbyRequest(request);
    }

    @Override
    public Uni<FindTenantLobbyRequestResponse> findTenantLobbyRequest(final FindTenantLobbyRequestRequest request) {
        return tenantService.findTenantLobbyRequest(request);
    }

    @Override
    public Uni<ViewTenantLobbyRequestsResponse> viewTenantLobbyRequests(
            final ViewTenantLobbyRequestsRequest request) {
        return tenantService.viewTenantLobbyRequests(request);
    }

    @Override
    public Uni<SyncTenantLobbyRequestResponse> syncTenantLobbyRequest(final SyncTenantLobbyRequestRequest request) {
        return tenantService.syncTenantLobbyRequest(request);
    }

    @Override
    public Uni<DeleteTenantLobbyRequestResponse> deleteTenantLobbyRequest(
            final DeleteTenantLobbyRequestRequest request) {
        return tenantService.deleteTenantLobbyRequest(request);
    }

    /*
    TenantLobbyRef
     */

    @Override
    public Uni<GetTenantLobbyRefResponse> getTenantLobbyRef(final GetTenantLobbyRefRequest request) {
        return tenantService.getTenantLobbyRef(request);
    }

    @Override
    public Uni<FindTenantLobbyRefResponse> findTenantLobbyRef(final FindTenantLobbyRefRequest request) {
        return tenantService.findTenantLobbyRef(request);
    }

    @Override
    public Uni<ViewTenantLobbyRefsResponse> viewTenantLobbyRefs(final ViewTenantLobbyRefsRequest request) {
        return tenantService.viewTenantLobbyRefs(request);
    }

    @Override
    public Uni<SyncTenantLobbyRefResponse> syncTenantLobbyRef(final SyncTenantLobbyRefRequest request) {
        return tenantService.syncTenantLobbyRef(request);
    }

    @Override
    public Uni<DeleteTenantLobbyRefResponse> deleteTenantLobbyRef(final DeleteTenantLobbyRefRequest request) {
        return tenantService.deleteTenantLobbyRef(request);
    }

    /*
    TenantMatchmakerRequest
     */

    @Override
    public Uni<GetTenantMatchmakerRequestResponse> getTenantMatchmakerRequest(
            final GetTenantMatchmakerRequestRequest request) {
        return tenantService.getTenantMatchmakerRequest(request);
    }

    @Override
    public Uni<FindTenantMatchmakerRequestResponse> findTenantMatchmakerRequest(
            final FindTenantMatchmakerRequestRequest request) {
        return tenantService.findTenantMatchmakerRequest(request);
    }

    @Override
    public Uni<ViewTenantMatchmakerRequestsResponse> viewTenantMatchmakerRequests(
            final ViewTenantMatchmakerRequestsRequest request) {
        return tenantService.viewTenantMatchmakerRequests(request);
    }

    @Override
    public Uni<SyncTenantMatchmakerRequestResponse> syncTenantMatchmakerRequest(
            final SyncTenantMatchmakerRequestRequest request) {
        return tenantService.syncTenantMatchmakerRequest(request);
    }

    @Override
    public Uni<DeleteTenantMatchmakerRequestResponse> deleteTenantMatchmakerRequest(
            final DeleteTenantMatchmakerRequestRequest request) {
        return tenantService.deleteTenantMatchmakerRequest(request);
    }

    /*
    TenantMatchmakerRef
     */

    @Override
    public Uni<GetTenantMatchmakerRefResponse> getTenantMatchmakerRef(final GetTenantMatchmakerRefRequest request) {
        return tenantService.getTenantMatchmakerRef(request);
    }

    @Override
    public Uni<SyncTenantMatchmakerRefResponse> syncTenantMatchmakerRef(
            final SyncTenantMatchmakerRefRequest request) {
        return tenantService.syncTenantMatchmakerRef(request);
    }

    @Override
    public Uni<FindTenantMatchmakerRefResponse> findTenantMatchmakerRef(
            final FindTenantMatchmakerRefRequest request) {
        return tenantService.findTenantMatchmakerRef(request);
    }

    @Override
    public Uni<ViewTenantMatchmakerRefsResponse> viewTenantMatchmakerRefs(
            final ViewTenantMatchmakerRefsRequest request) {
        return tenantService.viewTenantMatchmakerRefs(request);
    }

    @Override
    public Uni<DeleteTenantMatchmakerRefResponse> deleteTenantMatchmakerRef(
            final DeleteTenantMatchmakerRefRequest request) {
        return tenantService.deleteTenantMatchmakerRef(request);
    }
}
