package com.omgservers.dto.handler;

import com.omgservers.exception.ServerSideBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleStopRuntimeCommandRequest {

    static public void validate(final HandleStopRuntimeCommandRequest request) {
        if (request == null) {
            throw new ServerSideBadRequestException("request is null");
        }
        // TODO: validate fields
    }

    Long runtimeId;
}
