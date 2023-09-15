package com.omgservers.dto.user;

import com.omgservers.dto.ShardedRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAttributeRequest implements ShardedRequest {


    @NotNull
    Long userId;

    @NotNull
    Long playerId;

    @NotBlank
    @Size(max = 64)
    String name;

    @Override
    public String getRequestShardKey() {
        return userId.toString();
    }
}
