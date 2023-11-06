package com.omgservers.model.matchCommand;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = MatchCommandDeserializer.class)
public class MatchCommandModel {

    @NotNull
    Long id;

    @NotNull
    Long matchmakerId;

    @NotNull
    Long matchId;

    @NotNull
    Instant created;

    @NotNull
    Instant modified;

    @NotNull
    MatchCommandQualifierEnum qualifier;

    @NotNull
    MatchCommandBodyModel body;

    @NotNull
    Boolean deleted;
}
