package com.omgservers.service.entrypoint.player.impl.service.webService.impl.playerApi;

import com.omgservers.model.dto.player.CreateClientPlayerRequest;
import com.omgservers.model.dto.player.CreateClientPlayerResponse;
import com.omgservers.model.dto.player.CreateTokenPlayerRequest;
import com.omgservers.model.dto.player.CreateTokenPlayerResponse;
import com.omgservers.model.dto.player.CreateUserPlayerRequest;
import com.omgservers.model.dto.player.CreateUserPlayerResponse;
import com.omgservers.model.dto.player.InterchangePlayerRequest;
import com.omgservers.model.dto.player.InterchangePlayerResponse;
import com.omgservers.model.user.UserRoleEnum;
import com.omgservers.service.entrypoint.player.impl.service.webService.WebService;
import com.omgservers.service.operation.handleApiRequest.HandleApiRequestOperation;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
@RolesAllowed({UserRoleEnum.Names.PLAYER})
class PlayerApiImpl implements PlayerApi {

    final WebService webService;

    final HandleApiRequestOperation handleApiRequestOperation;

    @Override
    @PermitAll
    public Uni<CreateUserPlayerResponse> createUser(@NotNull final CreateUserPlayerRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::createUser);
    }

    @Override
    @PermitAll
    public Uni<CreateTokenPlayerResponse> createToken(@NotNull final CreateTokenPlayerRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::createToken);
    }

    @Override
    @RolesAllowed({UserRoleEnum.Names.PLAYER})
    public Uni<CreateClientPlayerResponse> createClient(@NotNull final CreateClientPlayerRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::createClient);
    }

    @Override
    @RolesAllowed({UserRoleEnum.Names.PLAYER})
    public Uni<InterchangePlayerResponse> interchange(@NotNull final InterchangePlayerRequest request) {
        return handleApiRequestOperation.handleApiRequest(log, request, webService::interchange);
    }
}
