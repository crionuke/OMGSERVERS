package com.omgservers.factory;

import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.model.project.ProjectConfigModel;
import com.omgservers.model.project.ProjectModel;
import com.omgservers.operation.generateId.GenerateIdOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class ProjectModelFactory {

    final GenerateIdOperation generateIdOperation;

    public ProjectModel create(Long tenantId, ProjectConfigModel config) {
        final var id = generateIdOperation.generateId();
        return create(id, tenantId, config);
    }

    public ProjectModel create(Long id, Long tenantId, ProjectConfigModel config) {
        if (id == null) {
            throw new ServerSideBadRequestException("id is null");
        }
        if (tenantId == null) {
            throw new ServerSideBadRequestException("tenantId is null");
        }
        if (config == null) {
            throw new ServerSideBadRequestException("config is null");
        }

        var now = Instant.now();

        ProjectModel project = new ProjectModel();
        project.setId(id);
        project.setTenantId(tenantId);
        project.setCreated(now);
        project.setModified(now);
        project.setConfig(config);
        return project;
    }
}
