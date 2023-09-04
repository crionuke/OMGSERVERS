package com.omgservers.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetIndexRequest {

    public static void validate(GetIndexRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }
    }

    String name;
}
