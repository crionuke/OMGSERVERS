package com.omgservers.application.module.tenantModule.impl.service.projectInternalService.impl.method.createProjectMethod;

import com.omgservers.application.module.internalModule.InternalModule;
import com.omgservers.application.module.internalModule.impl.service.eventHelpService.request.InsertEventHelpRequest;
import com.omgservers.application.module.internalModule.model.event.body.EventCreatedEventBodyModel;
import com.omgservers.application.module.tenantModule.impl.operation.insertProjectOperation.InsertProjectOperation;
import com.omgservers.application.module.tenantModule.impl.operation.insertStageOperation.InsertStageOperation;
import com.omgservers.application.module.tenantModule.impl.operation.validateProjectOperation.ValidateProjectOperation;
import com.omgservers.application.module.tenantModule.impl.operation.validateStageOperation.ValidateStageOperation;
import com.omgservers.application.operation.checkShardOperation.CheckShardOperation;
import com.omgservers.application.module.tenantModule.model.project.ProjectModel;
import com.omgservers.application.module.tenantModule.model.stage.StageModel;
import com.omgservers.application.module.internalModule.model.event.body.ProjectCreatedEventBodyModel;
import com.omgservers.application.module.internalModule.model.event.body.StageCreatedEventBodyModel;
import com.omgservers.application.module.tenantModule.impl.service.projectInternalService.request.CreateProjectInternalRequest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class CreateProjectMethodImpl implements CreateProjectMethod {

    final InternalModule internalModule;

    final ValidateProjectOperation validateProjectOperation;
    final ValidateStageOperation validateStageOperation;
    final InsertProjectOperation insertProjectOperation;
    final InsertStageOperation insertStageOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<Void> createProject(CreateProjectInternalRequest request) {
        CreateProjectInternalRequest.validate(request);

        final var project = request.getProject();
        final var stage = request.getStage();
        final var tenant = project.getTenantId();
        return Uni.createFrom().voidItem()
                .invoke(voidItem -> validateProjectOperation.validateProject(project))
                .invoke(voidItem -> validateStageOperation.validateStage(stage))
                .flatMap(validatedProject -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> createProjectAndStage(shardModel.shard(), tenant, project, stage));
    }

    Uni<Void> createProjectAndStage(Integer shard, Long tenantId, ProjectModel project, StageModel stage) {
        return pgPool.withTransaction(sqlConnection ->
                insertProjectOperation.insertProject(sqlConnection, shard, project)
                        .call(voidItem -> insertStageOperation.insertStage(sqlConnection, shard, stage))
                        .call(voidItem -> insertProjectCreatedEvent(sqlConnection, tenantId, project.getId()))
                        .call(voidItem -> insertStageCreatedEvent(sqlConnection, tenantId, stage.getId())));
    }

    Uni<Void> insertProjectCreatedEvent(SqlConnection sqlConnection, Long tenantId, Long id) {
        final var eventBody = new ProjectCreatedEventBodyModel(tenantId, id);
        final var insertEventInternalRequest = new InsertEventHelpRequest(sqlConnection, eventBody);
        return internalModule.getEventHelpService().insertEvent(insertEventInternalRequest)
                .replaceWithVoid();
    }

    Uni<Void> insertStageCreatedEvent(SqlConnection sqlConnection, Long tenantId, Long id) {
        final var eventBody = new StageCreatedEventBodyModel(tenantId, id);
        final var insertEventInternalRequest = new InsertEventHelpRequest(sqlConnection, eventBody);
        return internalModule.getEventHelpService().insertEvent(insertEventInternalRequest)
                .replaceWithVoid();
    }
}
