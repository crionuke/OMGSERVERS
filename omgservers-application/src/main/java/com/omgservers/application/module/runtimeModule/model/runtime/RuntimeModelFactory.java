package com.omgservers.application.module.runtimeModule.model.runtime;

import com.omgservers.application.operation.generateIdOperation.GenerateIdOperation;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class RuntimeModelFactory {

    final GenerateIdOperation generateIdOperation;

    public RuntimeModel create(final Long matchmakerId,
                               final Long matchId,
                               final RuntimeTypeEnum type,
                               final RuntimeConfigModel config) {
        final var id = generateIdOperation.generateId();
        return create(id, matchmakerId, matchId, type, config);
    }

    public RuntimeModel create(final Long id,
                               final Long matchmakerId,
                               final Long matchId,
                               final RuntimeTypeEnum type,
                               final RuntimeConfigModel config) {
        Instant now = Instant.now();

        final var runtime = new RuntimeModel();
        runtime.setId(id);
        runtime.setCreated(now);
        runtime.setModified(now);
        runtime.setMatchmakerId(matchmakerId);
        runtime.setMatchId(matchId);
        runtime.setType(type);
        runtime.setConfig(config);
        return runtime;
    }
}
