package com.omgservers.service.module.runtime.impl.operation.runtimeAssignment;

import com.omgservers.service.event.body.module.runtime.RuntimeAssignmentDeletedEventBodyModel;
import com.omgservers.service.factory.lobby.LogModelFactory;
import com.omgservers.service.operation.server.ChangeContext;
import com.omgservers.service.operation.server.ChangeObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class DeleteRuntimeAssignmentOperationImpl implements DeleteRuntimeAssignmentOperation {

    final ChangeObjectOperation changeObjectOperation;
    final LogModelFactory logModelFactory;

    @Override
    public Uni<Boolean> execute(final ChangeContext<?> changeContext,
                                final SqlConnection sqlConnection,
                                final int shard,
                                final Long runtimeId,
                                final Long id) {
        return changeObjectOperation.changeObject(
                changeContext, sqlConnection, shard,
                """
                        update $schema.tab_runtime_assignment
                        set modified = $3, deleted = true
                        where runtime_id = $1 and id = $2 and deleted = false
                        """,
                List.of(
                        runtimeId,
                        id,
                        Instant.now().atOffset(ZoneOffset.UTC)),
                () -> new RuntimeAssignmentDeletedEventBodyModel(runtimeId, id),
                () -> null
        );
    }
}
