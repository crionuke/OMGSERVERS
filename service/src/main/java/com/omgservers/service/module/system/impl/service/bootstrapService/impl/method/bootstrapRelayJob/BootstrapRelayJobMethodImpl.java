package com.omgservers.service.module.system.impl.service.bootstrapService.impl.method.bootstrapRelayJob;

import com.omgservers.schema.service.system.task.ExecuteRelayTaskRequest;
import com.omgservers.service.module.system.SystemModule;
import com.omgservers.service.operation.getConfig.GetConfigOperation;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduler;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class BootstrapRelayJobMethodImpl implements BootstrapRelayJobMethod {

    final SystemModule systemModule;

    final GetConfigOperation getConfigOperation;
    final Scheduler scheduler;

    @Override
    public Uni<Void> bootstrapRelayJob() {
        log.debug("Bootstrap relay job");

        return Uni.createFrom().voidItem()
                .invoke(voidItem -> {
                    final var interval = getConfigOperation.getServiceConfig().bootstrap().relayJob().interval();
                    final var trigger = scheduler.newJob("relay")
                            .setInterval(interval)
                            .setConcurrentExecution(Scheduled.ConcurrentExecution.SKIP)
                            .setAsyncTask(scheduledExecution -> {
                                final var request = new ExecuteRelayTaskRequest();
                                return systemModule.getTaskService().executeRelayTask(request)
                                        .replaceWithVoid();
                            })
                            .schedule();

                    log.info("Relay job was scheduled, {}", trigger);
                });
    }
}
