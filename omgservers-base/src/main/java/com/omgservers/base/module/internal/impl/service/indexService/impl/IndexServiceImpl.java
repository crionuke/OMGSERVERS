package com.omgservers.base.module.internal.impl.service.indexService.impl;

import com.omgservers.base.module.internal.impl.service.indexService.IndexService;
import com.omgservers.base.module.internal.impl.service.indexService.impl.method.deleteIndex.DeleteIndexMethod;
import com.omgservers.base.module.internal.impl.service.indexService.impl.method.getIndex.GetIndexMethod;
import com.omgservers.base.module.internal.impl.service.indexService.impl.method.syncIndex.SyncIndexMethod;
import com.omgservers.dto.internalModule.DeleteIndexRequest;
import com.omgservers.dto.internalModule.GetIndexRequest;
import com.omgservers.dto.internalModule.SyncIndexRequest;
import com.omgservers.dto.internalModule.GetIndexHelpResponse;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class IndexServiceImpl implements IndexService {

    final GetIndexMethod getIndexMethod;
    final SyncIndexMethod syncIndexMethod;
    final DeleteIndexMethod deleteIndexMethod;

    @Override
    public Uni<GetIndexHelpResponse> getIndex(GetIndexRequest request) {
        return getIndexMethod.getIndex(request);
    }

    @Override
    public Uni<Void> syncIndex(SyncIndexRequest request) {
        return syncIndexMethod.syncIndex(request);
    }

    @Override
    public Uni<Void> deleteIndex(DeleteIndexRequest request) {
        return deleteIndexMethod.deleteIndex(request);
    }
}
