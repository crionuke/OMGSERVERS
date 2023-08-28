package com.omgservers.module.internal.impl.service.indexService;

import com.omgservers.dto.internal.DeleteIndexRequest;
import com.omgservers.dto.internal.GetIndexRequest;
import com.omgservers.dto.internal.SyncIndexRequest;
import com.omgservers.dto.internal.GetIndexResponse;
import io.smallrye.mutiny.Uni;

public interface IndexService {

    Uni<GetIndexResponse> getIndex(GetIndexRequest request);

    Uni<Void> syncIndex(SyncIndexRequest request);

    Uni<Void> deleteIndex(DeleteIndexRequest request);
}
