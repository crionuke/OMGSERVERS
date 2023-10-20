package com.omgservers.dto.tenant;

import com.omgservers.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewVersionMatchmakersRequest implements ShardedRequest {

    @NotNull
    Long tenantId;

    @NotNull
    Long versionId;

    @NotNull
    Boolean deleted;

    @Override
    public String getRequestShardKey() {
        return tenantId.toString();
    }
}
