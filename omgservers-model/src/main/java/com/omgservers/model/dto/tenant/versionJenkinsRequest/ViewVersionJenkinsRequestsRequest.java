package com.omgservers.model.dto.tenant.versionJenkinsRequest;

import com.omgservers.model.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewVersionJenkinsRequestsRequest implements ShardedRequest {

    @NotNull
    Long tenantId;

    Long versionId;

    @Override
    public String getRequestShardKey() {
        return tenantId.toString();
    }
}
