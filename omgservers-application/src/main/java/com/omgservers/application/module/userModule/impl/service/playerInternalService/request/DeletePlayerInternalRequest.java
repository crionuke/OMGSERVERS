package com.omgservers.application.module.userModule.impl.service.playerInternalService.request;

import com.omgservers.application.request.InternalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletePlayerInternalRequest implements InternalRequest {

    static public void validate(DeletePlayerInternalRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    String user;
    UUID uuid;

    @Override
    public String getRequestShardKey() {
        return user.toString();
    }
}
