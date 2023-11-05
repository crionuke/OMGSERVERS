package com.omgservers.model.dto.tenant;

import com.omgservers.model.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewProjectsRequest implements ShardedRequest {

    @NotNull
    Long tenantId;

    @NotNull
    Boolean deleted;

    @Override
    public String getRequestShardKey() {
        return tenantId.toString();
    }
}
