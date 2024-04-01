package com.omgservers.model.dto.pool.poolRuntimeAssignment;

import com.omgservers.model.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPoolRuntimeAssignmentRequest implements ShardedRequest {

    @NotNull
    Long poolId;

    @NotNull
    Long runtimeId;

    @Override
    public String getRequestShardKey() {
        return poolId.toString();
    }
}
