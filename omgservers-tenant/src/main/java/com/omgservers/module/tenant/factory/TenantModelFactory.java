package com.omgservers.module.tenant.factory;

import com.omgservers.operation.generateId.GenerateIdOperation;
import com.omgservers.exception.ServerSideBadRequestException;
import com.omgservers.model.tenant.TenantConfigModel;
import com.omgservers.model.tenant.TenantModel;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class TenantModelFactory {

    final GenerateIdOperation generateIdOperation;

    public TenantModel create(final TenantConfigModel config) {
        final var id = generateIdOperation.generateId();
        return create(id, config);
    }

    public TenantModel create(final Long id, final TenantConfigModel config) {
        var now = Instant.now().truncatedTo(ChronoUnit.MILLIS);

        TenantModel tenant = new TenantModel();
        tenant.setId(id);
        tenant.setCreated(now);
        tenant.setModified(now);
        tenant.setConfig(config);
        return tenant;
    }
}
