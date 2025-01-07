package com.omgservers.schema.entrypoint.developer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTenantProjectDeveloperResponse {

    Long projectId;
    Long stageId;
}
