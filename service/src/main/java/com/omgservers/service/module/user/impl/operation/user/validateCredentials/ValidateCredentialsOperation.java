package com.omgservers.service.module.user.impl.operation.user.validateCredentials;

import com.omgservers.schema.model.user.UserModel;
import io.smallrye.mutiny.Uni;

public interface ValidateCredentialsOperation {
    Uni<UserModel> validateCredentials(UserModel user, String password);
}
