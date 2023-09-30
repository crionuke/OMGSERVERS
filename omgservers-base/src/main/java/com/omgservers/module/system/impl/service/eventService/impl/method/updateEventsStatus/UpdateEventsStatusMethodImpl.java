package com.omgservers.module.system.impl.service.eventService.impl.method.updateEventsStatus;

import com.omgservers.dto.internal.UpdateEventsStatusRequest;
import com.omgservers.dto.internal.UpdateEventsStatusResponse;
import com.omgservers.module.system.impl.operation.updateEventsStatusByIds.UpdateEventsStatusByIdsOperation;
import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.operation.changeWithContext.ChangeWithContextOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class UpdateEventsStatusMethodImpl implements UpdateEventsStatusMethod {

    final UpdateEventsStatusByIdsOperation updateEventsStatusByIdsOperation;
    final ChangeWithContextOperation changeWithContextOperation;

    @Override
    public Uni<UpdateEventsStatusResponse> updateEventsStatus(UpdateEventsStatusRequest request) {
        final var ids = request.getIds();
        final var status = request.getStatus();
        return changeWithContextOperation.<Boolean>changeWithContext(
                        (changeContext, sqlConnection) -> updateEventsStatusByIdsOperation
                                .updateEventsStatusByIds(changeContext, sqlConnection, ids, status)
                )
                .map(ChangeContext::getResult)
                .map(UpdateEventsStatusResponse::new);
    }
}
