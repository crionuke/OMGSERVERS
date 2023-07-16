package com.omgservers.application.module.tenantModule.impl.operation.validateStageOperation;

import com.omgservers.application.exception.ServerSideBadRequestException;
import com.omgservers.application.module.tenantModule.model.stage.StageModel;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ApplicationScoped
class ValidateStageOperationImpl implements ValidateStageOperation {

    @Override
    public StageModel validateStage(StageModel stage) {
        if (stage == null) {
            throw new IllegalArgumentException("stage is null");
        }

        var config = stage.getConfig();

        Map<String, Boolean> results = new HashMap<>();

        // TODO: validate stage

        var valid = results.values().stream().allMatch(Boolean.TRUE::equals);
        if (valid) {
            log.info("Stage is valid, stage={}", stage);
            return stage;
        } else {
            throw new ServerSideBadRequestException(String.format("bad stage, stage=%s", stage));
        }
    }
}
