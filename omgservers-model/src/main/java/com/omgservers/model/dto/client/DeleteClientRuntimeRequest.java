package com.omgservers.model.dto.client;

import com.omgservers.model.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteClientRuntimeRequest implements ShardedRequest {

    @NotNull
    Long clientId;

    @NotNull
    Long id;

    @Override
    public String getRequestShardKey() {
        return clientId.toString();
    }
}
