package com.omgservers.application.module.userModule.impl.service.attributeInternalService.request;

import com.omgservers.application.module.userModule.model.attribute.AttributeModel;
import com.omgservers.application.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncAttributeInternalRequest implements InternalRequest {

    static public void validate(SyncAttributeInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    Long userId;
    AttributeModel attribute;

    @Override
    public String getRequestShardKey() {
        return userId.toString();
    }
}
