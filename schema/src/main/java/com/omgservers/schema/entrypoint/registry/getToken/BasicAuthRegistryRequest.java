package com.omgservers.schema.entrypoint.registry.getToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestQuery;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthRegistryRequest {

    @RestQuery
    String account;

    @RestQuery
    String service;

    @RestQuery("offline_token")
    Boolean offlineToken;

    @RestQuery("client_id")
    String clientId;

    @RestQuery
    String scope;

    @RestHeader("Authorization")
    String authorizationHeader;
}
