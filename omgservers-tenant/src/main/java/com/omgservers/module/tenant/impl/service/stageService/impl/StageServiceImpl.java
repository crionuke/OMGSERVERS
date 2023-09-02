package com.omgservers.module.tenant.impl.service.stageService.impl;

import com.omgservers.dto.tenant.ValidateStageSecretRequest;
import com.omgservers.dto.tenant.ValidateStageSecretResponse;
import com.omgservers.module.tenant.impl.service.stageService.StageService;
import com.omgservers.module.tenant.impl.service.stageService.impl.method.validateStageSecret.ValidateStageSecretMethod;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class StageServiceImpl implements StageService {

    final ValidateStageSecretMethod validateStageSecretMethod;

    @Override
    public Uni<ValidateStageSecretResponse> validateStageSecret(final ValidateStageSecretRequest request) {
        return validateStageSecretMethod.validateStageSecret(request);
    }
}
