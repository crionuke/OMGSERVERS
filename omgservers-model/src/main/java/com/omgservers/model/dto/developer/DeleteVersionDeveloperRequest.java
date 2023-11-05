package com.omgservers.model.dto.developer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteVersionDeveloperRequest {

    @NotNull
    Long tenantId;

    @NotNull
    Long id;
}
