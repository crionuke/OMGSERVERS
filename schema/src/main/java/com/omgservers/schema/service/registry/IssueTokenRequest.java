package com.omgservers.schema.service.registry;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueTokenRequest {

    @NotNull
    Long userId;

    @NotNull
    Boolean offlineToken;

    String requestedScope;
}
