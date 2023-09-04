package com.omgservers.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeveloperAdminRequest {

    public static void validate(CreateDeveloperAdminRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    Long tenantId;
}
