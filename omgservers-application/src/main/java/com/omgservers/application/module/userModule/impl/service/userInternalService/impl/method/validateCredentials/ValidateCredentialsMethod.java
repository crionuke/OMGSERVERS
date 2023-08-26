package com.omgservers.application.module.userModule.impl.service.userInternalService.impl.method.validateCredentials;

import com.omgservers.dto.userModule.ValidateCredentialsRoutedRequest;
import com.omgservers.dto.userModule.ValidateCredentialsInternalResponse;
import io.smallrye.mutiny.Uni;

public interface ValidateCredentialsMethod {
    Uni<ValidateCredentialsInternalResponse> validateCredentials(ValidateCredentialsRoutedRequest request);
}
