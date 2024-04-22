package com.omgservers.model.dto.jenkins;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLuaJitWorkerBuilderV1Request {

    @NotNull
    Integer buildNumber;
}
