package com.omgservers.application.module.tenantModule.model.project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectConfigModel {

    static public ProjectConfigModel create() {
        ProjectConfigModel config = new ProjectConfigModel();
        return config;
    }
}
