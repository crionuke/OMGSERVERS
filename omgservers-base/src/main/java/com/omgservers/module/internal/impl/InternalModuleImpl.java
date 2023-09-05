package com.omgservers.module.internal.impl;

import com.omgservers.module.internal.InternalModule;
import com.omgservers.module.internal.impl.service.eventShardedService.EventShardedService;
import com.omgservers.module.internal.impl.service.indexService.IndexService;
import com.omgservers.module.internal.impl.service.jobShardedService.JobShardedService;
import com.omgservers.module.internal.impl.service.logService.LogService;
import com.omgservers.module.internal.impl.service.serviceAccountService.ServiceAccountService;
import com.omgservers.module.internal.impl.service.syncService.SyncService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class InternalModuleImpl implements InternalModule {

    final ServiceAccountService serviceAccountService;
    final EventShardedService eventShardedService;
    final JobShardedService jobShardedService;
    final IndexService indexService;
    final SyncService syncService;
    final LogService logService;

    public EventShardedService getEventShardedService() {
        return eventShardedService;
    }

    public IndexService getIndexService() {
        return indexService;
    }

    public JobShardedService getJobShardedService() {
        return jobShardedService;
    }

    public ServiceAccountService getServiceAccountService() {
        return serviceAccountService;
    }

    public SyncService getSyncService() {
        return syncService;
    }

    public LogService getLogService() {
        return logService;
    }
}
