package com.omgservers.model.stagePermission;

import com.omgservers.exception.ServerSideBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StagePermissionModel {

    public static void validate(StagePermissionModel permission) {
        if (permission == null) {
            throw new ServerSideBadRequestException("permission is null");
        }
    }

    Long id;
    Long tenantId;
    Long stageId;
    Instant created;
    Long userId;
    StagePermissionEnum permission;
}
