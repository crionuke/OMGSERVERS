package com.omgservers.service.job.handler;

import com.omgservers.model.dto.system.HandleEventsRequest;
import com.omgservers.model.dto.system.HandleEventsResponse;
import com.omgservers.model.handler.HandlerModel;
import com.omgservers.model.job.JobQualifierEnum;
import com.omgservers.service.factory.EventModelFactory;
import com.omgservers.service.module.system.SystemModule;
import com.omgservers.service.module.system.impl.service.jobService.impl.JobTask;
import com.omgservers.service.operation.getConfig.GetConfigOperation;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class HandlerJobTask implements JobTask {

    final SystemModule systemModule;

    final GetConfigOperation getConfigOperation;

    final EventModelFactory eventModelFactory;

    @Override
    public JobQualifierEnum getJobQualifier() {
        return JobQualifierEnum.HANDLER;
    }

    @Override
    public Uni<Void> executeTask(final Long shardKey, final Long entityId) {
        final var handlerId = entityId;

        return systemModule.getShortcutService().getHandler(handlerId)
                .map(handler -> {
                    if (handler.getDeleted()) {
                        log.info("Handler was deleted, skip job execution, handlerId={}", handlerId);
                        return null;
                    } else {
                        return handler;
                    }
                })
                .onItem().ifNotNull().transformToUni(this::handleHandler)
                .replaceWithVoid();
    }

    Uni<Void> handleHandler(final HandlerModel handler) {
        return Multi.createBy().repeating()
                .uni(this::handleEvents)
                .whilst(Boolean.TRUE::equals)
                .collect().last()
                .replaceWithVoid();
    }

    Uni<Boolean> handleEvents() {
        final var request = new HandleEventsRequest(getConfigOperation.getServiceConfig().handlerLimit());
        return systemModule.getHandlerService().handleEvents(request)
                .map(HandleEventsResponse::getResult);
    }
}
