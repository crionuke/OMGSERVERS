package com.omgservers.module.internal.impl.service.serviceAccountService.impl.method.syncServiceAccount;

import com.omgservers.operation.changeWithContext.ChangeContext;
import com.omgservers.dto.internal.SyncServiceAccountRequest;
import com.omgservers.module.internal.impl.operation.upsertServiceAccount.UpsertServiceAccountOperation;
import com.omgservers.operation.changeWithContext.ChangeWithContextOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class SyncServiceAccountMethodImpl implements SyncServiceAccountMethod {

    final UpsertServiceAccountOperation syncServiceAccountOperation;
    final ChangeWithContextOperation changeWithContextOperation;

    @Override
    public Uni<Void> syncServiceAccount(SyncServiceAccountRequest request) {
        SyncServiceAccountRequest.validate(request);

        final var serviceAccount = request.getServiceAccount();
        return changeWithContextOperation.<Boolean>changeWithContext((changeContext, sqlConnection) ->
                        syncServiceAccountOperation.upsertServiceAccount(changeContext, sqlConnection, serviceAccount))
                .map(ChangeContext::getResult)
                //TODO: make response with created flag
                .replaceWithVoid();
    }
}
