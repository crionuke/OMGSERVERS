package com.omgservers.service.module.system.impl.service.bootstrapService.impl.method.bootstrapSupportUser;

import com.omgservers.model.dto.user.GetUserRequest;
import com.omgservers.model.dto.user.GetUserResponse;
import com.omgservers.model.dto.user.SyncUserRequest;
import com.omgservers.model.dto.user.SyncUserResponse;
import com.omgservers.model.user.UserModel;
import com.omgservers.model.user.UserRoleEnum;
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
class BootstrapSupportUserMethodImpl implements BootstrapSupportUserMethod {

    final UserModule userModule;

    final GetConfigOperation getConfigOperation;

    final UserModelFactory userModelFactory;

    @Override
    public Uni<Void> bootstrapSupportUser() {
        log.debug("Bootstrap support user");

        final var userId = getConfigOperation.getServiceConfig().defaults().supportId();
        return getUser(userId)
                .invoke(root -> log.info("Support user was already create, skip operation, userId={}", userId))
                .onFailure(ServerSideNotFoundException.class)
                .recoverWithUni(t -> {
                    final var idempotencyKey = "bootstrap/support";
                    final var password = getConfigOperation.getServiceConfig().bootstrap().support().password();
                    final var passwordHash = BcryptUtil.bcryptHash(password);
                    final var user = userModelFactory.create(userId,
                            UserRoleEnum.SUPPORT,
                            passwordHash, idempotencyKey);

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
