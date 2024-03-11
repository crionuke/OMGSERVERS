package com.omgservers.model.projectPermission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPermissionModel {

    @NotNull
    Long id;

    @NotNull
    Long tenantId;

    @NotNull
    Long projectId;

    @NotNull
    Instant created;

    @NotNull
    Instant modified;

    @NotBlank
    String idempotencyKey;

    @NotNull
    Long userId;

    @NotNull
    ProjectPermissionEnum permission;

    @NotNull
    Boolean deleted;
}
