package com.omgservers.model.lobbyRuntime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LobbyRuntimeModel {

    @NotNull
    Long id;

    @NotNull
    Long lobbyId;

    @NotNull
    Instant created;

    @NotNull
    Instant modified;

    @NotNull
    Long runtimeId;

    @NotNull
    Boolean deleted;
}
