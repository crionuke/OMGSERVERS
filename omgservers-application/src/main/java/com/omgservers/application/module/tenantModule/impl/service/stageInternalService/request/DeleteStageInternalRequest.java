package com.omgservers.application.module.tenantModule.impl.service.stageInternalService.request;

import com.omgservers.application.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteStageInternalRequest implements InternalRequest {

    static public void validate(DeleteStageInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
        // TODO: validate fields
    }

    Long tenantId;
    Long id;

    @Override
    public String getRequestShardKey() {
        return tenantId.toString();
    }
}
