package com.omgservers.application.module.userModule.impl.service.objectInternalService.request;

import com.omgservers.application.module.userModule.model.object.ObjectModel;
import com.omgservers.application.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncObjectInternalRequest implements InternalRequest {

    static public void validate(SyncObjectInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    Long userId;
    ObjectModel object;

    @Override
    public String getRequestShardKey() {
        return userId.toString();
    }
}
