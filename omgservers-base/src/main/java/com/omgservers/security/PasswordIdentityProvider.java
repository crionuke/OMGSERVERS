package com.omgservers.security;

import com.omgservers.dto.internal.ValidateCredentialsRequest;
import com.omgservers.dto.internal.ValidateCredentialsResponse;
import com.omgservers.model.internalRole.InternalRoleEnum;
import com.omgservers.module.system.SystemModule;
import com.omgservers.operation.getConfig.GetConfigOperation;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class PasswordIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

    final SystemModule systemModule;
    final GetConfigOperation getConfigOperation;

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    @ActivateRequestContext
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request,
                                              AuthenticationRequestContext context) {
        final var username = request.getUsername();
        final var password = new String(request.getPassword().getPassword());

        if (username.equals(getConfigOperation.getConfig().adminUsername())) {
            if (BcryptUtil.matches(password, getConfigOperation.getConfig().adminPasswordHash())) {
                final var principal = "admin/" + username;
                log.debug("Admin account was authenticated, principal={}", principal);
                return Uni.createFrom().item(QuarkusSecurityIdentity.builder()
                        .setPrincipal(new QuarkusPrincipal(principal))
                        .addRole(InternalRoleEnum.Names.ADMIN)
                        .setAnonymous(false)
                        .build());
            } else {
                log.warn("Authentication failed, username={}", username);
                throw new AuthenticationFailedException();
            }
        } else {
            final var validateCredentialsInternalRequest = new ValidateCredentialsRequest(username, password);
            return systemModule.getServiceAccountService().validateCredentials(validateCredentialsInternalRequest)
                    .map(ValidateCredentialsResponse::getValid)
                    .map(valid -> {
                        if (valid) {
                            final var principal = "sa/" + username;
                            log.debug("Service account was authenticated, principal={}", principal);
                            return (SecurityIdentity) QuarkusSecurityIdentity.builder()
                                    .setPrincipal(new QuarkusPrincipal(principal))
                                    .addRole(InternalRoleEnum.Names.SERVICE)
                                    .setAnonymous(false)
                                    .build();
                        } else {
                            log.warn("Authentication failed, username={}", username);
                            throw new AuthenticationFailedException();
                        }
                    })
                    .onFailure().transform(t -> {
                        log.warn("Authentication failed, {}", t.getMessage());
                        return new AuthenticationFailedException();
                    });
        }
    }
}
