package com.omgservers.schema.entrypoint.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientPlayerRequest {

    @NotBlank
    @Size(max = 64)
    String tenant;

    @NotBlank
    @Size(max = 64)
    String project;

    @NotBlank
    @Size(max = 64)
    String stage;
}
