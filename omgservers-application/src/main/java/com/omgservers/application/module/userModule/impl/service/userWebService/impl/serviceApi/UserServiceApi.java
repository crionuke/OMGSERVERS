package com.omgservers.application.module.userModule.impl.service.userWebService.impl.serviceApi;

import com.omgservers.application.module.userModule.impl.service.attributeInternalService.request.DeleteAttributeInternalRequest;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.request.GetAttributeInternalRequest;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.request.GetPlayerAttributesInternalRequest;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.request.SyncAttributeInternalRequest;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.response.DeleteAttributeInternalResponse;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.response.GetAttributeInternalResponse;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.response.GetPlayerAttributesInternalResponse;
import com.omgservers.application.module.userModule.impl.service.attributeInternalService.response.SyncAttributeInternalResponse;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.request.DeleteClientInternalRequest;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.request.GetClientInternalRequest;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.request.SyncClientInternalRequest;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.response.DeleteClientInternalResponse;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.response.GetClientInternalResponse;
import com.omgservers.application.module.userModule.impl.service.clientInternalService.response.SyncClientInternalResponse;
import com.omgservers.application.module.userModule.impl.service.objectInternalService.request.DeleteObjectInternalRequest;
import com.omgservers.application.module.userModule.impl.service.objectInternalService.request.GetObjectInternalRequest;
import com.omgservers.application.module.userModule.impl.service.objectInternalService.request.SyncObjectInternalRequest;
import com.omgservers.application.module.userModule.impl.service.objectInternalService.response.DeleteObjectInternalResponse;
import com.omgservers.application.module.userModule.impl.service.objectInternalService.response.GetObjectInternalResponse;
import com.omgservers.application.module.userModule.impl.service.objectInternalService.response.SyncObjectInternalResponse;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.request.DeletePlayerInternalRequest;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.request.GetPlayerInternalRequest;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.request.SyncPlayerInternalRequest;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.response.DeletePlayerInternalResponse;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.response.GetPlayerInternalResponse;
import com.omgservers.application.module.userModule.impl.service.playerInternalService.response.SyncPlayerInternalResponse;
import com.omgservers.application.module.userModule.impl.service.tokenInternalService.request.CreateTokenInternalRequest;
import com.omgservers.application.module.userModule.impl.service.tokenInternalService.request.IntrospectTokenInternalRequest;
import com.omgservers.application.module.userModule.impl.service.tokenInternalService.response.CreateTokenInternalResponse;
import com.omgservers.application.module.userModule.impl.service.tokenInternalService.response.IntrospectTokenInternalResponse;
import com.omgservers.application.module.userModule.impl.service.userInternalService.request.SyncUserInternalRequest;
import com.omgservers.application.module.userModule.impl.service.userInternalService.request.ValidateCredentialsInternalRequest;
import com.omgservers.application.module.userModule.impl.service.userInternalService.response.SyncUserInternalResponse;
import com.omgservers.application.module.userModule.impl.service.userInternalService.response.ValidateCredentialsInternalResponse;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

import java.time.Duration;

@Path("/omgservers/service-api/user-api/v1/request")
public interface UserServiceApi {

    @PUT
    @Path("/sync-user")
    Uni<SyncUserInternalResponse> syncUser(SyncUserInternalRequest request);

    default SyncUserInternalResponse syncUser(long timeout, SyncUserInternalRequest request) {
        return syncUser(request).await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/validate-credentials")
    Uni<ValidateCredentialsInternalResponse> validateCredentials(ValidateCredentialsInternalRequest request);

    @PUT
    @Path("/create-token")
    Uni<CreateTokenInternalResponse> createToken(CreateTokenInternalRequest request);

    @PUT
    @Path("/introspect-token")
    Uni<IntrospectTokenInternalResponse> introspectToken(IntrospectTokenInternalRequest request);

    @PUT
    @Path("/get-player")
    Uni<GetPlayerInternalResponse> getPlayer(GetPlayerInternalRequest request);

    @PUT
    @Path("/sync-player")
    Uni<SyncPlayerInternalResponse> syncPlayer(SyncPlayerInternalRequest request);

    @PUT
    @Path("/delete-player")
    Uni<DeletePlayerInternalResponse> deletePlayer(DeletePlayerInternalRequest request);

    default DeletePlayerInternalResponse deletePlayer(long timeout, DeletePlayerInternalRequest request) {
        return deletePlayer(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/sync-client")
    Uni<SyncClientInternalResponse> syncClient(SyncClientInternalRequest request);

    @PUT
    @Path("/get-client")
    Uni<GetClientInternalResponse> getClient(GetClientInternalRequest request);

    @PUT
    @Path("/delete-client")
    Uni<DeleteClientInternalResponse> deleteClient(DeleteClientInternalRequest request);

    default DeleteClientInternalResponse deleteClient(long timeout, DeleteClientInternalRequest request) {
        return deleteClient(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/get-attribute")
    Uni<GetAttributeInternalResponse> getAttribute(GetAttributeInternalRequest request);

    default GetAttributeInternalResponse getAttribute(long timeout, GetAttributeInternalRequest request) {
        return getAttribute(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/get-player-attributes")
    Uni<GetPlayerAttributesInternalResponse> getPlayerAttributes(GetPlayerAttributesInternalRequest request);

    default GetPlayerAttributesInternalResponse getPlayerAttributes(long timeout, GetPlayerAttributesInternalRequest request) {
        return getPlayerAttributes(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/sync-attribute")
    Uni<SyncAttributeInternalResponse> syncAttribute(SyncAttributeInternalRequest request);

    default SyncAttributeInternalResponse syncAttribute(long timeout, SyncAttributeInternalRequest request) {
        return syncAttribute(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/delete-attribute")
    Uni<DeleteAttributeInternalResponse> deleteAttribute(DeleteAttributeInternalRequest request);

    default DeleteAttributeInternalResponse deleteAttribute(long timeout, DeleteAttributeInternalRequest request) {
        return deleteAttribute(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/get-object")
    Uni<GetObjectInternalResponse> getObject(GetObjectInternalRequest request);

    default GetObjectInternalResponse getObject(long timeout, GetObjectInternalRequest request) {
        return getObject(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/sync-object")
    Uni<SyncObjectInternalResponse> syncObject(SyncObjectInternalRequest request);

    default SyncObjectInternalResponse syncObject(long timeout, SyncObjectInternalRequest request) {
        return syncObject(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }

    @PUT
    @Path("/delete-object")
    Uni<DeleteObjectInternalResponse> deleteObject(DeleteObjectInternalRequest request);

    default DeleteObjectInternalResponse deleteObject(long timeout, DeleteObjectInternalRequest request) {
        return deleteObject(request)
                .await().atMost(Duration.ofSeconds(timeout));
    }
}
