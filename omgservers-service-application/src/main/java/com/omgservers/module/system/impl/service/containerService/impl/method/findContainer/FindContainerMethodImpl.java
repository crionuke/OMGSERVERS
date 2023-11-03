package com.omgservers.module.system.impl.service.containerService.impl.method.findContainer;

import com.omgservers.model.dto.system.FindContainerRequest;
import com.omgservers.model.dto.system.FindContainerResponse;
import com.omgservers.module.system.impl.operation.selectContainerByEntityIdAndQualifier.SelectContainerByEntityIdAndQualifierOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class FindContainerMethodImpl implements FindContainerMethod {

    final SelectContainerByEntityIdAndQualifierOperation selectContainerByEntityIdAndQualifierOperation;

    final PgPool pgPool;

    @Override
    public Uni<FindContainerResponse> findContainer(final FindContainerRequest request) {
        final var entityId = request.getEntityId();
        final var qualifier = request.getQualifier();
        final var deleted = request.getDeleted();
        return pgPool.withTransaction(sqlConnection -> selectContainerByEntityIdAndQualifierOperation
                        .selectContainerByEntityIdAndQualifier(sqlConnection, entityId, qualifier, deleted))
                .map(FindContainerResponse::new);
    }
}
