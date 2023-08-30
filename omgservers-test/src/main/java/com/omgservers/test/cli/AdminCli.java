package com.omgservers.test.cli;

import com.omgservers.dto.admin.CreateDeveloperAdminRequest;
import com.omgservers.dto.admin.CreateDeveloperAdminResponse;
import com.omgservers.dto.admin.CreateTenantAdminRequest;
import com.omgservers.dto.admin.DeleteIndexAdminRequest;
import com.omgservers.dto.admin.DeleteServiceAccountAdminRequest;
import com.omgservers.dto.admin.GenerateIdAdminResponse;
import com.omgservers.dto.admin.GetIndexAdminRequest;
import com.omgservers.dto.admin.GetServiceAccountAdminRequest;
import com.omgservers.dto.admin.PingServerAdminResponse;
import com.omgservers.dto.admin.SyncIndexAdminRequest;
import com.omgservers.dto.admin.SyncServiceAccountAdminRequest;
import com.omgservers.model.index.IndexConfigModel;
import com.omgservers.model.index.IndexModel;
import com.omgservers.model.serviceAccount.ServiceAccountModel;
import com.omgservers.test.operations.getAdminClientOperation.AdminClientForAdminAccount;
import com.omgservers.test.operations.getAdminClientOperation.GetAdminClientOperation;
import com.omgservers.test.operations.getConfigOperation.GetConfigOperation;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.Instant;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AdminCli {
    private final int TIMEOUT = 10;

    final GetConfigOperation getConfigOperation;
    final GetAdminClientOperation getAdminClientOperation;

    AdminClientForAdminAccount adminClient;

    public void createClient() {
        final var uri = getConfigOperation.getServers().get(0).externalAddress();
        createClient(uri);
    }

    public void createClient(URI uri) {
        adminClient = getAdminClientOperation.getAdminClientForAdminAccount(uri);
    }

    public AdminClientForAdminAccount getClient() {
        return adminClient;
    }

    public PingServerAdminResponse pingServer() {
        return adminClient.pingServer(TIMEOUT);
    }

    public GenerateIdAdminResponse generateId() {
        return adminClient.generateId(TIMEOUT);
    }

    public IndexModel getIndex(String name) {
        final var getIndexResponse = adminClient.getIndex(TIMEOUT, new GetIndexAdminRequest(name));
        return getIndexResponse.getIndex();
    }

    public IndexModel createIndex(String name, IndexConfigModel indexConfig) {
        final var now = Instant.now();
        final var index = new IndexModel(generateId().getId(), now, now, name, 1L, indexConfig);
        adminClient.syncIndex(TIMEOUT, new SyncIndexAdminRequest(index));
        return index;
    }

    public void deleteIndex(Long id) {
        adminClient.deleteIndex(TIMEOUT, new DeleteIndexAdminRequest(id));
    }

    public ServiceAccountModel getServiceAccount(String username) {
        final var getServiceAccountResponse = adminClient
                .getServiceAccount(TIMEOUT, new GetServiceAccountAdminRequest(username));
        return getServiceAccountResponse.getServiceAccount();
    }

    public void createServiceAccount(String username, String password) {
        final var now = Instant.now();
        final var passwordHash = BcryptUtil.bcryptHash(password);
        final var serviceAccount = new ServiceAccountModel(generateId().getId(), now, now, username, passwordHash);
        syncServiceAccount(serviceAccount);
    }

    public void syncServiceAccount(ServiceAccountModel serviceAccount) {
        adminClient.syncServiceAccount(TIMEOUT, new SyncServiceAccountAdminRequest(serviceAccount));
    }

    public void deleteServiceAccount(Long id) {
        adminClient.deleteServiceAccount(TIMEOUT, new DeleteServiceAccountAdminRequest(id));
    }

    public Long createTenant(String title) {
        final var response = adminClient.createTenant(TIMEOUT, new CreateTenantAdminRequest());
        return response.getId();
    }

    public CreateDeveloperAdminResponse createDeveloper(Long tenantId) {
        return adminClient.createDeveloper(TIMEOUT, new CreateDeveloperAdminRequest(tenantId));
    }
}
