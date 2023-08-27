package com.omgservers.module.internal.impl.service.syncService;

import com.omgservers.dto.internal.SyncIndexOverServersInternalRequest;
import com.omgservers.dto.internal.SyncServiceAccountOverServersInternalRequest;
import io.smallrye.mutiny.Uni;

public interface SyncService {

    Uni<Void> syncIndex(SyncIndexOverServersInternalRequest request);

    Uni<Void> syncServiceAccount(SyncServiceAccountOverServersInternalRequest request);
}
