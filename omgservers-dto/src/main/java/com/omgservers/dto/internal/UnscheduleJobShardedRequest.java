package com.omgservers.dto.internal;

import com.omgservers.dto.ShardedRequest;
import com.omgservers.exception.ServerSideBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnscheduleJobShardedRequest implements ShardedRequest {

    public static void validate(UnscheduleJobShardedRequest request) {
        if (request == null) {
            throw new ServerSideBadRequestException("request is null");
        }
    }

    Long shardKey;
    Long entity;

    @Override
    public String getRequestShardKey() {
        return shardKey.toString();
    }
}
