package com.omgservers.module.tenant.impl.service.projectService.impl.method.syncProject;

import com.omgservers.dto.tenant.SyncProjectRequest;
import com.omgservers.dto.tenant.SyncProjectResponse;
import com.omgservers.model.project.ProjectModel;
import com.omgservers.model.shard.ShardModel;
import com.omgservers.module.tenant.impl.operation.upsertProject.UpsertProjectOperation;
import com.omgservers.module.tenant.impl.operation.validateProject.ValidateProjectOperation;
import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.operation.changeWithContext.ChangeWithContextOperation;
import com.omgservers.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncProjectMethodImpl implements SyncProjectMethod {

    final ChangeWithContextOperation changeWithContextOperation;
    final ValidateProjectOperation validateProjectOperation;
    final UpsertProjectOperation upsertProjectOperation;
    final CheckShardOperation checkShardOperation;

    final PgPool pgPool;

    @Override
    public Uni<SyncProjectResponse> syncProject(SyncProjectRequest request) {
        final var project = request.getProject();
        validateProjectOperation.validateProject(project);

        return Uni.createFrom().voidItem()
                .flatMap(voidItem -> checkShardOperation.checkShard(request.getRequestShardKey()))
                .flatMap(shardModel -> changeFunction(shardModel, project))
                .map(SyncProjectResponse::new);
    }

    Uni<Boolean> changeFunction(ShardModel shardModel, ProjectModel project) {
        return changeWithContextOperation.<Boolean>changeWithContext((changeContext, sqlConnection) ->
                        upsertProjectOperation.upsertProject(changeContext, sqlConnection, shardModel.shard(), project))
                .map(ChangeContext::getResult);
    }
}
