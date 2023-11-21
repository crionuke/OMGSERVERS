package com.omgservers.tester.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omgservers.model.dto.developer.CreateProjectDeveloperRequest;
import com.omgservers.model.dto.developer.CreateProjectDeveloperResponse;
import com.omgservers.model.dto.developer.CreateTokenDeveloperRequest;
import com.omgservers.model.dto.developer.CreateTokenDeveloperResponse;
import com.omgservers.model.dto.developer.CreateVersionDeveloperRequest;
import com.omgservers.model.dto.developer.CreateVersionDeveloperResponse;
import com.omgservers.model.dto.developer.DeleteVersionDeveloperRequest;
import com.omgservers.model.dto.developer.DeleteVersionDeveloperResponse;
import com.omgservers.model.version.VersionConfigModel;
import com.omgservers.model.version.VersionSourceCodeModel;
import com.omgservers.tester.operation.getConfig.GetConfigOperation;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DeveloperApiTester {

    final GetConfigOperation getConfigOperation;

    final ObjectMapper objectMapper;

    public String createDeveloperToken(final Long userId,
                                       final String password) throws JsonProcessingException {
        final var responseSpecification = RestAssured
                .with()
                .baseUri(getConfigOperation.getConfig().gatewayUri().toString())
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(new CreateTokenDeveloperRequest(userId, password)))
                .log().method().log().uri().log().headers().log().body(false)
                .when().put("/omgservers/developer-api/v1/request/create-token");
        responseSpecification.then().statusCode(200);

        final var response = responseSpecification.getBody().as(CreateTokenDeveloperResponse.class);
        return response.getRawToken();
    }

    public CreateProjectDeveloperResponse createProject(String token, Long tenantId) throws JsonProcessingException {
        final var responseSpecification = RestAssured
                .with()
                .baseUri(getConfigOperation.getConfig().gatewayUri().toString())
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(new CreateProjectDeveloperRequest(tenantId)))
                .log().method().log().uri().log().headers().log().body(false)
                .when().put("/omgservers/developer-api/v1/request/create-project");
        responseSpecification.then().statusCode(200);

        final var response = responseSpecification.getBody().as(CreateProjectDeveloperResponse.class);
        return response;
    }

    public CreateVersionDeveloperResponse createVersion(String token, Long tenantId, Long stageId,
                                                        VersionConfigModel versionConfig,
                                                        VersionSourceCodeModel sourceCode)
            throws JsonProcessingException {
        final var responseSpecification = RestAssured
                .with()
                .baseUri(getConfigOperation.getConfig().gatewayUri().toString())
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(
                        new CreateVersionDeveloperRequest(tenantId, stageId, versionConfig, sourceCode)))
                .log().method().log().uri().log().headers().log().body(false)
                .when().put("/omgservers/developer-api/v1/request/create-version");
        responseSpecification.then().statusCode(200);

        final var response = responseSpecification.getBody().as(CreateVersionDeveloperResponse.class);
        return response;
    }

    public DeleteVersionDeveloperResponse deleteVersion(String token, Long tenantId, Long id)
            throws JsonProcessingException {
        final var responseSpecification = RestAssured
                .with()
                .baseUri(getConfigOperation.getConfig().gatewayUri().toString())
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(
                        new DeleteVersionDeveloperRequest(tenantId, id)))
                .log().method().log().uri().log().headers().log().body(false)
                .when().put("/omgservers/developer-api/v1/request/delete-version");
        responseSpecification.then().statusCode(200);

        final var response = responseSpecification.getBody().as(DeleteVersionDeveloperResponse.class);
        return response;
    }
}
