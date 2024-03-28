package com.omgservers.model.event;

import com.omgservers.model.event.body.internal.ClientMessageReceivedEventBodyModel;
import com.omgservers.model.event.body.internal.InactiveClientDetectedEventBodyModel;
import com.omgservers.model.event.body.internal.InactiveRuntimeDetectedEventBodyModel;
import com.omgservers.model.event.body.internal.LobbyAssignmentRequestedEventBodyModel;
import com.omgservers.model.event.body.internal.MatchmakerAssignmentRequestedEventBodyModel;
import com.omgservers.model.event.body.internal.MatchmakerMessageReceivedEventBodyModel;
import com.omgservers.model.event.body.job.MatchmakerJobTaskExecutionRequestedEventBodyModel;
import com.omgservers.model.event.body.job.RuntimeJobTaskExecutionRequestedEventBodyModel;
import com.omgservers.model.event.body.job.StageJobTaskExecutionRequestedEventBodyModel;
import com.omgservers.model.event.body.module.client.ClientCreatedEventBodyModel;
import com.omgservers.model.event.body.module.client.ClientDeletedEventBodyModel;
import com.omgservers.model.event.body.module.client.ClientMatchmakerRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.client.ClientMatchmakerRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.client.ClientRuntimeRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.client.ClientRuntimeRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.lobby.LobbyCreatedEventBodyModel;
import com.omgservers.model.event.body.module.lobby.LobbyDeletedEventBodyModel;
import com.omgservers.model.event.body.module.lobby.LobbyRuntimeRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.lobby.LobbyRuntimeRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerAssignmentCreatedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerAssignmentDeletedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerCreatedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerDeletedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerMatchClientCreatedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerMatchClientDeletedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerMatchCreatedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerMatchDeletedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerMatchRuntimeRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerMatchRuntimeRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerRequestCreatedEventBodyModel;
import com.omgservers.model.event.body.module.matchmaker.MatchmakerRequestDeletedEventBodyModel;
import com.omgservers.model.event.body.module.pool.PoolCreatedEventBodyModel;
import com.omgservers.model.event.body.module.pool.PoolDeletedEventBodyModel;
import com.omgservers.model.event.body.module.pool.PoolServerRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.pool.PoolServerRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.root.RootCreatedEventBodyModel;
import com.omgservers.model.event.body.module.root.RootDeletedEventBodyModel;
import com.omgservers.model.event.body.module.runtime.RuntimeAssignmentCreatedEventBodyModel;
import com.omgservers.model.event.body.module.runtime.RuntimeAssignmentDeletedEventBodyModel;
import com.omgservers.model.event.body.module.runtime.RuntimeCreatedEventBodyModel;
import com.omgservers.model.event.body.module.runtime.RuntimeDeletedEventBodyModel;
import com.omgservers.model.event.body.module.server.ServerContainerCreatedEventBodyModel;
import com.omgservers.model.event.body.module.server.ServerContainerDeletedEventBodyModel;
import com.omgservers.model.event.body.module.server.ServerCreatedEventBodyModel;
import com.omgservers.model.event.body.module.server.ServerDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.ProjectCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.ProjectDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.StageCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.StageDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.TenantCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.TenantDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionLobbyRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionLobbyRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionLobbyRequestCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionLobbyRequestDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionMatchmakerRefCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionMatchmakerRefDeletedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionMatchmakerRequestCreatedEventBodyModel;
import com.omgservers.model.event.body.module.tenant.VersionMatchmakerRequestDeletedEventBodyModel;
import com.omgservers.model.event.body.module.user.PlayerCreatedEventBodyModel;
import com.omgservers.model.event.body.module.user.PlayerDeletedEventBodyModel;
import com.omgservers.model.event.body.module.user.UserCreatedEventBodyModel;
import com.omgservers.model.event.body.module.user.UserDeletedEventBodyModel;
import com.omgservers.model.event.body.system.ContainerCreatedEventBodyModel;
import com.omgservers.model.event.body.system.ContainerDeletedEventBodyModel;
import com.omgservers.model.event.body.system.IndexCreatedEventBodyModel;
import com.omgservers.model.event.body.system.IndexDeletedEventBodyModel;
import com.omgservers.model.event.body.system.ServiceAccountCreatedEventBodyModel;
import com.omgservers.model.event.body.system.ServiceAccountDeletedEventBodyModel;

public enum EventQualifierEnum {
    // System
    INDEX_CREATED(IndexCreatedEventBodyModel.class, true),
    INDEX_DELETED(IndexDeletedEventBodyModel.class, true),
    SERVICE_ACCOUNT_CREATED(ServiceAccountCreatedEventBodyModel.class, true),
    SERVICE_ACCOUNT_DELETED(ServiceAccountDeletedEventBodyModel.class, true),
    CONTAINER_CREATED(ContainerCreatedEventBodyModel.class, true),
    CONTAINER_DELETED(ContainerDeletedEventBodyModel.class, true),
    // Module
    ROOT_CREATED(RootCreatedEventBodyModel.class, false),
    ROOT_DELETED(RootDeletedEventBodyModel.class, false),
    POOL_CREATED(PoolCreatedEventBodyModel.class, false),
    POOL_DELETED(PoolDeletedEventBodyModel.class, false),
    POOL_SERVER_REF_CREATED(PoolServerRefCreatedEventBodyModel.class, false),
    POOL_SERVER_REF_DELETED(PoolServerRefDeletedEventBodyModel.class, false),
    SERVER_CREATED(ServerCreatedEventBodyModel.class, false),
    SERVER_DELETED(ServerDeletedEventBodyModel.class, false),
    SERVER_CONTAINER_CREATED(ServerContainerCreatedEventBodyModel.class, false),
    SERVER_CONTAINER_DELETED(ServerContainerDeletedEventBodyModel.class, false),
    TENANT_CREATED(TenantCreatedEventBodyModel.class, true),
    TENANT_DELETED(TenantDeletedEventBodyModel.class, true),
    PROJECT_CREATED(ProjectCreatedEventBodyModel.class, true),
    PROJECT_DELETED(ProjectDeletedEventBodyModel.class, true),
    STAGE_CREATED(StageCreatedEventBodyModel.class, true),
    STAGE_DELETED(StageDeletedEventBodyModel.class, true),
    VERSION_LOBBY_REQUEST_CREATED(VersionLobbyRequestCreatedEventBodyModel.class, true),
    VERSION_LOBBY_REQUEST_DELETED(VersionLobbyRequestDeletedEventBodyModel.class, true),
    VERSION_LOBBY_REF_CREATED(VersionLobbyRefCreatedEventBodyModel.class, false),
    VERSION_LOBBY_REF_DELETED(VersionLobbyRefDeletedEventBodyModel.class, false),
    VERSION_MATCHMAKER_REQUEST_CREATED(VersionMatchmakerRequestCreatedEventBodyModel.class, true),
    VERSION_MATCHMAKER_REQUEST_DELETED(VersionMatchmakerRequestDeletedEventBodyModel.class, true),
    VERSION_MATCHMAKER_REF_CREATED(VersionMatchmakerRefCreatedEventBodyModel.class, false),
    VERSION_MATCHMAKER_REF_DELETED(VersionMatchmakerRefDeletedEventBodyModel.class, false),
    VERSION_CREATED(VersionCreatedEventBodyModel.class, true),
    VERSION_DELETED(VersionDeletedEventBodyModel.class, true),
    USER_CREATED(UserCreatedEventBodyModel.class, true),
    USER_DELETED(UserDeletedEventBodyModel.class, true),
    PLAYER_CREATED(PlayerCreatedEventBodyModel.class, true),
    PLAYER_DELETED(PlayerDeletedEventBodyModel.class, true),
    CLIENT_CREATED(ClientCreatedEventBodyModel.class, true),
    CLIENT_DELETED(ClientDeletedEventBodyModel.class, true),
    CLIENT_RUNTIME_REF_CREATED(ClientRuntimeRefCreatedEventBodyModel.class, false),
    CLIENT_RUNTIME_REF_DELETED(ClientRuntimeRefDeletedEventBodyModel.class, false),
    CLIENT_MATCHMAKER_REF_CREATED(ClientMatchmakerRefCreatedEventBodyModel.class, false),
    CLIENT_MATCHMAKER_REF_DELETED(ClientMatchmakerRefDeletedEventBodyModel.class, false),
    LOBBY_CREATED(LobbyCreatedEventBodyModel.class, true),
    LOBBY_DELETED(LobbyDeletedEventBodyModel.class, true),
    LOBBY_RUNTIME_REF_CREATED(LobbyRuntimeRefCreatedEventBodyModel.class, false),
    LOBBY_RUNTIME_REF_DELETED(LobbyRuntimeRefDeletedEventBodyModel.class, false),
    MATCHMAKER_CREATED(MatchmakerCreatedEventBodyModel.class, true),
    MATCHMAKER_DELETED(MatchmakerDeletedEventBodyModel.class, true),
    MATCHMAKER_MATCH_CREATED(MatchmakerMatchCreatedEventBodyModel.class, true),
    MATCHMAKER_MATCH_DELETED(MatchmakerMatchDeletedEventBodyModel.class, true),
    MATCHMAKER_MATCH_CLIENT_CREATED(MatchmakerMatchClientCreatedEventBodyModel.class, true),
    MATCHMAKER_MATCH_CLIENT_DELETED(MatchmakerMatchClientDeletedEventBodyModel.class, true),
    MATCHMAKER_MATCH_RUNTIME_REF_CREATED(MatchmakerMatchRuntimeRefCreatedEventBodyModel.class, false),
    MATCHMAKER_MATCH_RUNTIME_REF_DELETED(MatchmakerMatchRuntimeRefDeletedEventBodyModel.class, false),
    MATCHMAKER_REQUEST_CREATED(MatchmakerRequestCreatedEventBodyModel.class, true),
    MATCHMAKER_REQUEST_DELETED(MatchmakerRequestDeletedEventBodyModel.class, true),
    MATCHMAKER_ASSIGNMENT_CREATED(MatchmakerAssignmentCreatedEventBodyModel.class, true),
    MATCHMAKER_ASSIGNMENT_DELETED(MatchmakerAssignmentDeletedEventBodyModel.class, true),
    RUNTIME_CREATED(RuntimeCreatedEventBodyModel.class, true),
    RUNTIME_DELETED(RuntimeDeletedEventBodyModel.class, true),
    RUNTIME_ASSIGNMENT_CREATED(RuntimeAssignmentCreatedEventBodyModel.class, true),
    RUNTIME_ASSIGNMENT_DELETED(RuntimeAssignmentDeletedEventBodyModel.class, true),
    // Internal
    CLIENT_MESSAGE_RECEIVED(ClientMessageReceivedEventBodyModel.class, false),
    MATCHMAKER_MESSAGE_RECEIVED(MatchmakerMessageReceivedEventBodyModel.class, false),
    INACTIVE_CLIENT_DETECTED(InactiveClientDetectedEventBodyModel.class, false),
    INACTIVE_RUNTIME_DETECTED(InactiveRuntimeDetectedEventBodyModel.class, false),
    LOBBY_ASSIGNMENT_REQUESTED(LobbyAssignmentRequestedEventBodyModel.class, false),
    MATCHMAKER_ASSIGNMENT_REQUESTED(MatchmakerAssignmentRequestedEventBodyModel.class, false),
    // Job
    STAGE_JOB_TASK_EXECUTION_REQUESTED(StageJobTaskExecutionRequestedEventBodyModel.class, false),
    MATCHMAKER_JOB_TASK_EXECUTION_REQUESTED(MatchmakerJobTaskExecutionRequestedEventBodyModel.class, false),
    RUNTIME_JOB_TASK_EXECUTION_REQUESTED(RuntimeJobTaskExecutionRequestedEventBodyModel.class, false);

    final Class<? extends EventBodyModel> bodyClass;
    final boolean forward;

    EventQualifierEnum(final Class<? extends EventBodyModel> bodyClass,
                       final Boolean forward) {
        this.bodyClass = bodyClass;
        this.forward = forward;
    }

    public Class<? extends EventBodyModel> getBodyClass() {
        return bodyClass;
    }

    public boolean isForward() {
        return forward;
    }
}
