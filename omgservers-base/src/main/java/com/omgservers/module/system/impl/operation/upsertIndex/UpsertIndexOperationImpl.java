package com.omgservers.module.system.impl.operation.upsertIndex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.model.event.body.IndexCreatedEventBodyModel;
import com.omgservers.model.index.IndexModel;
import com.omgservers.module.system.factory.LogModelFactory;
import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.operation.changeObject.ChangeObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpsertIndexOperationImpl implements UpsertIndexOperation {

    final ChangeObjectOperation changeObjectOperation;

    final LogModelFactory logModelFactory;
    final ObjectMapper objectMapper;

    @Override
    public Uni<Boolean> upsertIndex(final ChangeContext<?> changeContext,
                                    final SqlConnection sqlConnection,
                                    final IndexModel index) {
        return changeObjectOperation.changeObject(
                changeContext, sqlConnection, 0,
                """
                        insert into system.tab_index(id, created, modified, name, version, config)
                        values($1, $2, $3, $4, $5, $6)
                        on conflict (id) do
                        nothing
                        """,
                Arrays.asList(
                        index.getId(),
                        index.getCreated().atOffset(ZoneOffset.UTC),
                        index.getModified().atOffset(ZoneOffset.UTC),
                        index.getName(),
                        index.getVersion(),
                        getConfigString(index)
                ),
                () -> new IndexCreatedEventBodyModel(index.getId()),
                () -> logModelFactory.create("Index was created, index=" + index)
        );
    }

    String getConfigString(IndexModel index) {
        try {
            return objectMapper.writeValueAsString(index.getConfig());
        } catch (IOException e) {
            throw new ServerSideBadRequestException(e.getMessage(), e);
        }
    }
}
