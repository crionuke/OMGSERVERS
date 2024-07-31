package com.omgservers.schema.entrypoint.developer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageDto {

    Long id;

    Instant created;

    List<StagePermissionDto> permissions;
}
