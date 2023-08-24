package com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.impl.method.deleteActorMethod;

import com.omgservers.application.module.internalModule.InternalModule;
import com.omgservers.application.module.internalModule.model.log.LogModelFactory;
import com.omgservers.application.module.runtimeModule.impl.operation.deleteActorOperation.DeleteActorOperation;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.request.DeleteActorInternalRequest;
import com.omgservers.application.module.runtimeModule.impl.service.runtimeInternalService.response.DeleteActorInternalResponse;
import com.omgservers.application.operation.changeOperation.ChangeOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = lombok.AccessLevel.PACKAGE)
class DeleteActorMethodImpl implements DeleteActorMethod {

    final InternalModule internalModule;

    final DeleteActorOperation deleteActorOperation;
    final ChangeOperation changeOperation;

    final LogModelFactory logModelFactory;
    final PgPool pgPool;

    @Override
    public Uni<DeleteActorInternalResponse> deleteActor(DeleteActorInternalRequest request) {
        DeleteActorInternalRequest.validate(request);

        final var runtimeId = request.getRuntimeId();
        final var id = request.getId();
        return changeOperation.changeWithLog(request,
                        (sqlConnection, shardModel) -> deleteActorOperation
                                .deleteActor(sqlConnection, shardModel.shard(), id),
                        deleted -> {
                            if (deleted) {
                                return logModelFactory.create(String.format("Actor was deleted, " +
                                        "runtimeId=%d, id=%d", runtimeId, id));
                            } else {
                                return null;
                            }
                        }
                )
                .map(DeleteActorInternalResponse::new);
    }
}
