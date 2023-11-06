package com.omgservers.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestModel {

    @NotNull
    Long id;

    @NotNull
    Long matchmakerId;

    @NotNull
    @EqualsAndHashCode.Exclude
    Instant created;

    @NotNull
    @EqualsAndHashCode.Exclude
    Instant modified;

    @NotNull
    Long userId;

    @NotNull
    Long clientId;

    @NotNull
    @Size(max = 64)
    String mode;

    @NotNull
    @EqualsAndHashCode.Exclude
    RequestConfigModel config;

    @NotNull
    Boolean deleted;
}
