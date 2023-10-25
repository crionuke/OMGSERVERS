package com.omgservers.dto.runtime;

import com.omgservers.dto.ShardedRequest;
import com.omgservers.model.runtimeCommand.RuntimeCommandStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewRuntimeCommandsRequest implements ShardedRequest {

    @NotNull
    Long runtimeId;

    @NotNull
    Boolean deleted;

    @Override
    public String getRequestShardKey() {
        return runtimeId.toString();
    }
}
