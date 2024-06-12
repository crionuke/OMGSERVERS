package com.omgservers.tester.operation.bootstrapTestVersion;

import com.omgservers.model.version.VersionConfigModel;
import com.omgservers.tester.component.AdminApiTester;
import com.omgservers.tester.component.DeveloperApiTester;
import com.omgservers.tester.component.SupportApiTester;
import com.omgservers.tester.model.TestVersionModel;
import com.omgservers.tester.operation.createBase64Archive.CreateBase64ArchiveOperation;
import com.omgservers.tester.operation.getLuaFile.GetLuaFileOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class BootstrapTestVersionOperationImpl implements BootstrapTestVersionOperation {

    CreateBase64ArchiveOperation createBase64ArchiveOperation;
    GetLuaFileOperation getLuaFileOperation;

    DeveloperApiTester developerApiTester;
    SupportApiTester supportApiTester;
    AdminApiTester adminApiTester;

    @Override
    public TestVersionModel bootstrapTestVersion(final String mainLua) throws IOException {
        return bootstrapTestVersion(mainLua, VersionConfigModel.create());
    }

    @Override
    public TestVersionModel bootstrapTestVersion(final String mainLua,
                                                 final VersionConfigModel versionConfig) throws IOException {
        final var adminToken = adminApiTester.createAdminToken();

        final var supportToken = supportApiTester.createSupportToken();
        final var tenantId = supportApiTester.createTenant(supportToken);

        final var createDeveloperAdminResponse = supportApiTester.createDeveloper(supportToken, tenantId);
        final var developerUserId = createDeveloperAdminResponse.getUserId();
        final var developerPassword = createDeveloperAdminResponse.getPassword();

        final var developerToken = developerApiTester.createDeveloperToken(developerUserId, developerPassword);
        final var createProjectDeveloperResponse = developerApiTester.createProject(developerToken, tenantId);
        final var projectId = createProjectDeveloperResponse.getProjectId();
        final var stageId = createProjectDeveloperResponse.getStageId();
        final var stageSecret = createProjectDeveloperResponse.getSecret();

        final var base64Archive = createBase64ArchiveOperation.createBase64Archive(Map.of(
                        "main.lua", mainLua,
                        "omgservers.lua", getLuaFileOperation.getOmgserversLua()
                )
        );

        final var createVersionDeveloperResponse = developerApiTester
                .createVersion(developerToken, tenantId, stageId, versionConfig, base64Archive);
        final var versionId = createVersionDeveloperResponse.getId();

        return TestVersionModel.builder()
                .adminToken(adminToken)
                .supportToken(supportToken)
                .tenantId(tenantId)
                .developerUserId(developerUserId)
                .developerPassword(developerPassword)
                .developerToken(developerToken)
                .projectId(projectId)
                .stageId(stageId)
                .stageSecret(stageSecret)
                .versionId(versionId)
                .build();
    }
}
