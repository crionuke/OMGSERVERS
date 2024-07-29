package com.omgservers.service.module.system.impl.service.bootstrapService.impl.method.bootstrapBuilderUser;

import com.omgservers.schema.model.user.UserModel;
import com.omgservers.schema.model.user.UserRoleEnum;
import com.omgservers.schema.module.user.GetUserRequest;
import com.omgservers.schema.module.user.GetUserResponse;
import com.omgservers.schema.module.user.SyncUserRequest;
import com.omgservers.schema.module.user.SyncUserResponse;
import com.omgservers.service.exception.ServerSideNotFoundException;
import com.omgservers.service.factory.user.UserModelFactory;
import com.omgservers.service.module.user.UserModule;
import com.omgservers.service.operation.getConfig.GetConfigOperation;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class BootstrapBuilderUserMethodImpl implements BootstrapBuilderUserMethod {

    final UserModule userModule;

    final GetConfigOperation getConfigOperation;

    final UserModelFactory userModelFactory;

    @Override
    public Uni<Void> bootstrapBuilderUser() {
        log.debug("Bootstrap builder user");

        final var userId = getConfigOperation.getServiceConfig().defaults().builderUserId();
        return getUser(userId)
                .invoke(user -> log.info("Builder user was already create, skip operation, userId={}", userId))
                .onFailure(ServerSideNotFoundException.class)
                .recoverWithUni(t -> {
                    final var idempotencyKey = "bootstrap/builderUser";
                    final var password = getConfigOperation.getServiceConfig().bootstrap().builderUser().password();
                    final var passwordHash = BcryptUtil.bcryptHash(password);
                    final var user = userModelFactory.create(userId,
                            UserRoleEnum.BUILDER,
                            passwordHash,
                            idempotencyKey);

                    return syncUser(user)
                            .replaceWith(user);
                })
                .replaceWithVoid();
    }

    Uni<UserModel> getUser(final Long userId) {
        final var request = new GetUserRequest(userId);
        return userModule.getUserService().getUser(request)
                .map(GetUserResponse::getUser);
    }

    Uni<Boolean> syncUser(final UserModel userModel) {
        final var request = new SyncUserRequest(userModel);
        return userModule.getUserService().syncUserWithIdempotency(request)
                .map(SyncUserResponse::getCreated);
    }
}
