package com.omgservers.service.entrypoint.developer.impl.service.developerService;

import com.omgservers.model.dto.developer.CreateProjectDeveloperRequest;
import com.omgservers.model.dto.developer.CreateProjectDeveloperResponse;
import com.omgservers.model.dto.developer.CreateTokenDeveloperRequest;
import com.omgservers.model.dto.developer.CreateTokenDeveloperResponse;
import com.omgservers.model.dto.developer.CreateVersionDeveloperRequest;
import com.omgservers.model.dto.developer.CreateVersionDeveloperResponse;
import com.omgservers.model.dto.developer.DeleteVersionDeveloperRequest;
import com.omgservers.model.dto.developer.DeleteVersionDeveloperResponse;
import com.omgservers.model.dto.developer.DeployVersionDeveloperRequest;
import com.omgservers.model.dto.developer.DeployVersionDeveloperResponse;
import com.omgservers.model.dto.developer.GetTenantDashboardDeveloperRequest;
import com.omgservers.model.dto.developer.GetTenantDashboardDeveloperResponse;
import com.omgservers.model.dto.developer.UploadVersionDeveloperRequest;
import com.omgservers.model.dto.developer.UploadVersionDeveloperResponse;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;

public interface DeveloperService {

    Uni<CreateTokenDeveloperResponse> createToken(@Valid CreateTokenDeveloperRequest request);

    Uni<GetTenantDashboardDeveloperResponse> getTenantDashboard(@Valid GetTenantDashboardDeveloperRequest request);

    Uni<CreateProjectDeveloperResponse> createProject(@Valid CreateProjectDeveloperRequest request);

    Uni<CreateVersionDeveloperResponse> createVersion(@Valid CreateVersionDeveloperRequest request);

    Uni<UploadVersionDeveloperResponse> uploadVersion(@Valid UploadVersionDeveloperRequest request);

    Uni<DeployVersionDeveloperResponse> deployVersion(@Valid DeployVersionDeveloperRequest request);

    Uni<DeleteVersionDeveloperResponse> deleteVersion(@Valid DeleteVersionDeveloperRequest request);
}
