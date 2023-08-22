package com.omgservers.application.module.internalModule.model.event;

import com.omgservers.application.module.internalModule.model.event.body.ClientCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.ClientDisconnectedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.ClientUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.EventCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.JobCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.JobDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.JobUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchmakerCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchmakerDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchmakerRequestedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.MatchmakerUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.PlayerCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.PlayerSignedInEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.PlayerSignedUpEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.ProjectCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.ProjectDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.ProjectUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.RuntimeCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.RuntimeDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.RuntimeUpdatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.SignInRequestedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.SignUpRequestedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.StageCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.StageDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.TenantCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.TenantDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.VersionCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.VersionDeletedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.VersionUpdatedEventBodyModel;

public enum EventQualifierEnum {
    EVENT_CREATED(EventCreatedEventBodyModel.class),
    JOB_CREATED(JobCreatedEventBodyModel.class),
    JOB_UPDATED(JobUpdatedEventBodyModel.class),
    JOB_DELETED(JobDeletedEventBodyModel.class),
    TENANT_CREATED(TenantCreatedEventBodyModel.class),
    TENANT_UPDATED(TenantCreatedEventBodyModel.class),
    TENANT_DELETED(TenantDeletedEventBodyModel.class),
    PROJECT_CREATED(ProjectCreatedEventBodyModel.class),
    PROJECT_UPDATED(ProjectUpdatedEventBodyModel.class),
    PROJECT_DELETED(ProjectDeletedEventBodyModel.class),
    STAGE_CREATED(StageCreatedEventBodyModel.class),
    STAGE_DELETED(StageDeletedEventBodyModel.class),
    VERSION_CREATED(VersionCreatedEventBodyModel.class),
    VERSION_UPDATED(VersionUpdatedEventBodyModel.class),
    VERSION_DELETED(VersionDeletedEventBodyModel.class),
    PLAYER_CREATED(PlayerCreatedEventBodyModel.class),
    PLAYER_UPDATED(PlayerCreatedEventBodyModel.class),
    CLIENT_CREATED(ClientCreatedEventBodyModel.class),
    CLIENT_UPDATED(ClientUpdatedEventBodyModel.class),
    CLIENT_DISCONNECTED(ClientDisconnectedEventBodyModel.class),
    MATCHMAKER_CREATED(MatchmakerCreatedEventBodyModel.class),
    MATCHMAKER_UPDATED(MatchmakerUpdatedEventBodyModel.class),
    MATCHMAKER_DELETED(MatchmakerDeletedEventBodyModel.class),
    MATCH_CREATED(MatchCreatedEventBodyModel.class),
    MATCH_UPDATED(MatchUpdatedEventBodyModel.class),
    MATCH_DELETED(MatchDeletedEventBodyModel.class),
    RUNTIME_CREATED(RuntimeCreatedEventBodyModel.class),
    RUNTIME_UPDATED(RuntimeUpdatedEventBodyModel.class),
    RUNTIME_DELETED(RuntimeDeletedEventBodyModel.class),
    SIGN_UP_REQUESTED(SignUpRequestedEventBodyModel.class),
    SIGN_IN_REQUESTED(SignInRequestedEventBodyModel.class),
    MATCHMAKER_REQUESTED(MatchmakerRequestedEventBodyModel.class),
    PLAYER_SIGNED_UP(PlayerSignedUpEventBodyModel.class),
    PLAYER_SIGNED_IN(PlayerSignedInEventBodyModel.class);

    Class<? extends EventBodyModel> bodyClass;

    EventQualifierEnum(Class<? extends EventBodyModel> bodyClass) {
        this.bodyClass = bodyClass;
    }

    public Class<? extends EventBodyModel> getBodyClass() {
        return bodyClass;
    }
}
