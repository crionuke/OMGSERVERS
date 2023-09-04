package com.omgservers.dto.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewLogRequest {

    public static void validate(ViewLogRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }
}
