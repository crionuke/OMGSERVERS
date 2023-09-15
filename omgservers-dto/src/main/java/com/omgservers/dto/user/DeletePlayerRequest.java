package com.omgservers.dto.user;

import com.omgservers.dto.ShardedRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletePlayerRequest implements ShardedRequest {

    @NotNull
    Long userId;

    @NotNull
    Long id;

    @Override
    public String getRequestShardKey() {
        return userId.toString();
    }
}
