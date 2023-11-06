package com.omgservers.model.dto.system;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetIndexRequest {

    @NotNull
    Long id;

    @NotNull
    Boolean deleted;
}
