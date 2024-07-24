package com.omgservers.service.entrypoint.router.impl.service.routerService.impl.method.createToken;

import com.omgservers.model.dto.router.CreateTokenRouterRequest;
import com.omgservers.model.dto.router.CreateTokenRouterResponse;
import com.omgservers.model.dto.user.CreateTokenRequest;
import com.omgservers.model.dto.user.CreateTokenResponse;
import com.omgservers.service.module.user.UserModule;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CreateTokenMethodImpl implements CreateTokenMethod {

    final UserModule userModule;

    @Override
    public Uni<CreateTokenRouterResponse> createToken(final CreateTokenRouterRequest request) {
        log.debug("Create token, request={}", request);

        final var userId = request.getUserId();
        final var password = request.getPassword();

        return createToken(userId, password)
                .map(CreateTokenRouterResponse::new);
    }

    Uni<String> createToken(final Long userId, final String password) {
        final var createTokenRequest = new CreateTokenRequest(userId, password);
        return userModule.getUserService().createToken(createTokenRequest)
                .map(CreateTokenResponse::getRawToken);
    }
}
