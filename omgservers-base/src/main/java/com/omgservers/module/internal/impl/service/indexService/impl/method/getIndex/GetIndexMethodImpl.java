package com.omgservers.module.internal.impl.service.indexService.impl.method.getIndex;

import com.omgservers.module.internal.impl.operation.getIndex.GetIndexOperation;
import com.omgservers.dto.internal.GetIndexRequest;
import com.omgservers.dto.internal.GetIndexResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class GetIndexMethodImpl implements GetIndexMethod {

    final PgPool pgPool;
    final GetIndexOperation getIndexOperation;

    @Override
    public Uni<GetIndexResponse> getIndex(final GetIndexRequest request) {
        GetIndexRequest.validate(request);

        final var name = request.getName();
        return pgPool.withTransaction(sqlConnection -> getIndexOperation
                        .getIndex(sqlConnection, name))
                .map(GetIndexResponse::new);
    }
}
