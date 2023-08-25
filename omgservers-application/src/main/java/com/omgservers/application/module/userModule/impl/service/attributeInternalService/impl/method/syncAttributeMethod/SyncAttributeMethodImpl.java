package com.omgservers.application.module.userModule.impl.service.attributeInternalService.impl.method.syncAttributeMethod;

import com.omgservers.application.module.userModule.impl.operation.upsertAttributeOperation.UpsertAttributeOperation;
import com.omgservers.base.impl.operation.changeOperation.ChangeOperation;
import com.omgservers.dto.userModule.SyncAttributeInternalRequest;
import com.omgservers.dto.userModule.SyncAttributeInternalResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class SyncAttributeMethodImpl implements SyncAttributeMethod {

    final UpsertAttributeOperation upsertAttributeOperation;
    final ChangeOperation changeOperation;

    final PgPool pgPool;

    @Override
    public Uni<SyncAttributeInternalResponse> syncAttribute(SyncAttributeInternalRequest request) {
        SyncAttributeInternalRequest.validate(request);

        final var attribute = request.getAttribute();
        return changeOperation.change(request,
                        ((sqlConnection, shardModel) -> upsertAttributeOperation
                                .upsertAttribute(sqlConnection, shardModel.shard(), attribute)))
                .map(SyncAttributeInternalResponse::new);
    }
}
