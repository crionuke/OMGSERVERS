package com.omgservers.module.admin.impl.service.adminService;

import com.omgservers.dto.admin.CollectLogsAdminRequest;
import com.omgservers.dto.admin.CollectLogsAdminResponse;
import com.omgservers.dto.admin.CreateDeveloperAdminRequest;
import com.omgservers.dto.admin.CreateDeveloperAdminResponse;
import com.omgservers.dto.admin.CreateTenantAdminRequest;
import com.omgservers.dto.admin.CreateTenantAdminResponse;
import com.omgservers.dto.admin.GenerateIdAdminResponse;
import com.omgservers.dto.admin.PingServerAdminResponse;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;

public interface AdminService {
    Uni<PingServerAdminResponse> pingServer();

    Uni<GenerateIdAdminResponse> generateId();

    Uni<CreateTenantAdminResponse> createTenant(@Valid CreateTenantAdminRequest request);

    Uni<CreateDeveloperAdminResponse> createDeveloper(@Valid CreateDeveloperAdminRequest request);

    Uni<CollectLogsAdminResponse> collectLogs(@Valid CollectLogsAdminRequest request);
}
