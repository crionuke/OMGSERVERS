package com.omgservers.module.system.impl;

import com.omgservers.module.system.SystemModule;
import com.omgservers.module.system.impl.service.eventService.EventService;
import com.omgservers.module.system.impl.service.indexService.IndexService;
import com.omgservers.module.system.impl.service.jobService.JobService;
import com.omgservers.module.system.impl.service.logService.LogService;
import com.omgservers.module.system.impl.service.serviceAccountService.ServiceAccountService;
import com.omgservers.module.system.impl.service.syncService.SyncService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SystemModuleImpl implements SystemModule {

    final ServiceAccountService serviceAccountService;
    final EventService eventService;
    final JobService jobService;
    final IndexService indexService;
    final SyncService syncService;
    final LogService logService;

    public EventService getEventService() {
        return eventService;
    }

    public IndexService getIndexService() {
        return indexService;
    }

    public JobService getJobService() {
        return jobService;
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
