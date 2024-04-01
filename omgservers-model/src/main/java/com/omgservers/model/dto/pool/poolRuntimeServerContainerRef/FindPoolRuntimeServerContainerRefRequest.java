package com.omgservers.model.dto.pool.poolRuntimeServerContainerRef;

import com.omgservers.model.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPoolRuntimeServerContainerRefRequest implements ShardedRequest {

    @NotNull
    Long poolId;

    @NotNull
    Long serverId;

    @NotNull
    Long containerId;

    @Override
    public String getRequestShardKey() {
        return poolId.toString();
    }
}
