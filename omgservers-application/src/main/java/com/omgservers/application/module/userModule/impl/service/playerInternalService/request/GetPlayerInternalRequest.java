package com.omgservers.application.module.userModule.impl.service.playerInternalService.request;

import com.omgservers.application.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerInternalRequest implements InternalRequest {

    static public void validate(GetPlayerInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    Long userId;
    Long stageId;

    @Override
    public String getRequestShardKey() {
        return userId.toString();
    }
}
