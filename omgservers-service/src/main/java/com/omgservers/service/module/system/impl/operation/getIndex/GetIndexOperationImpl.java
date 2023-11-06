package com.omgservers.service.module.system.impl.operation.getIndex;

import com.omgservers.model.index.IndexModel;
import com.omgservers.service.module.system.impl.mappers.IndexModelMapper;
import com.omgservers.service.operation.selectObject.SelectObjectOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.SqlConnection;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetIndexOperationImpl implements GetIndexOperation {

    final SelectObjectOperation selectObjectOperation;

    final IndexModelMapper indexModelMapper;

    @Override
    public Uni<IndexModel> getIndex(final SqlConnection sqlConnection,
                                    final Long id,
                                    final Boolean deleted) {
        return selectObjectOperation.selectObject(
                sqlConnection,
                0,
                """
                        select id, created, modified, name, version, config
                        from system.tab_index
                        where id = $1 and deleted = $2
                        limit 1
                        """,
                Arrays.asList(id, deleted),
                "Index",
                indexModelMapper::fromRow);
    }
}
