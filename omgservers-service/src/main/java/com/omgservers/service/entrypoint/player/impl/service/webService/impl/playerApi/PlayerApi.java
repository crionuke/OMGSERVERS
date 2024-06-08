package com.omgservers.service.entrypoint.player.impl.service.webService.impl.playerApi;

import com.omgservers.model.dto.player.CreateClientPlayerRequest;
import com.omgservers.model.dto.player.CreateClientPlayerResponse;
import com.omgservers.model.dto.player.CreateTokenPlayerRequest;
import com.omgservers.model.dto.player.CreateTokenPlayerResponse;
import com.omgservers.model.dto.player.CreateUserPlayerRequest;
import com.omgservers.model.dto.player.CreateUserPlayerResponse;
import com.omgservers.model.dto.player.InterchangePlayerRequest;
import com.omgservers.model.dto.player.InterchangePlayerResponse;
import com.omgservers.service.configuration.ServiceOpenApiConfiguration;
import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Player Entrypoint API")
@Path("/omgservers/v1/entrypoint/player/request")
@SecurityRequirement(name = ServiceOpenApiConfiguration.PLAYER_SECURITY_SCHEMA)
public interface PlayerApi {

    @PUT
    @Path("/create-user")
    Uni<CreateUserPlayerResponse> createUser(@NotNull CreateUserPlayerRequest request);

    @PUT
    @Path("/create-token")
    Uni<CreateTokenPlayerResponse> createToken(@NotNull CreateTokenPlayerRequest request);

    @PUT
    @Path("/create-client")
    Uni<CreateClientPlayerResponse> createClient(@NotNull CreateClientPlayerRequest request);

    @PUT
    @Path("/interchange")
    Uni<InterchangePlayerResponse> interchange(@NotNull InterchangePlayerRequest request);
}
