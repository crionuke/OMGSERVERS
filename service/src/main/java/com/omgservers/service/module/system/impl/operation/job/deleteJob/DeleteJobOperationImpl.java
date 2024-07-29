package com.omgservers.service.module.system.impl.operation.job.deleteJob;

import com.omgservers.schema.event.body.system.JobDeletedEventBodyModel;
import com.omgservers.service.factory.lobby.LogModelFactory;
import com.omgservers.service.operation.changeObject.ChangeObjectOperation;
import com.omgservers.service.operation.changeWithContext.ChangeContext;
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
class DeleteJobOperationImpl implements DeleteJobOperation {

    final ChangeObjectOperation changeObjectOperation;
    final LogModelFactory logModelFactory;

    @Override
    public Uni<Boolean> deleteJob(final ChangeContext<?> changeContext,
                                  final SqlConnection sqlConnection,
                                  final Long id) {
        return changeObjectOperation.changeObject(
                changeContext, sqlConnection, 0,
                """
                        update system.tab_job
                        set modified = $2, deleted = true
                        where id = $1 and deleted = false
                        """,
                List.of(
                        id,
                        Instant.now().atOffset(ZoneOffset.UTC)
                ),
                () -> new JobDeletedEventBodyModel(id),
                () -> null
        );
    }
}
