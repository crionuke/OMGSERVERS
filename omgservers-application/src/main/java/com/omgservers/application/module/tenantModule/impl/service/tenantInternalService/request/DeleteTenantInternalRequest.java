package com.omgservers.application.module.tenantModule.impl.service.tenantInternalService.request;

import com.omgservers.application.request.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTenantInternalRequest implements InternalRequest {

    static public void validate(DeleteTenantInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    UUID uuid;

    @Override
    public String getRequestShardKey() {
        return uuid.toString();
    }
}
