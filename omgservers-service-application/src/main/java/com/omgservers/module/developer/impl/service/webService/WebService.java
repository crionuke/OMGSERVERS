package com.omgservers.module.developer.impl.service.webService;

import com.omgservers.model.dto.developer.CreateProjectDeveloperRequest;
import com.omgservers.model.dto.developer.CreateProjectDeveloperResponse;
import com.omgservers.model.dto.developer.CreateTokenDeveloperRequest;
import com.omgservers.model.dto.developer.CreateTokenDeveloperResponse;
import com.omgservers.model.dto.developer.CreateVersionDeveloperRequest;
import com.omgservers.model.dto.developer.CreateVersionDeveloperResponse;
import io.smallrye.mutiny.Uni;

public interface WebService {

    Uni<CreateTokenDeveloperResponse> createToken(CreateTokenDeveloperRequest request);

    Uni<CreateProjectDeveloperResponse> createProject(CreateProjectDeveloperRequest request);

    Uni<CreateVersionDeveloperResponse> createVersion(CreateVersionDeveloperRequest request);
}
