package com.omgservers.service.service.initializer.impl.method;

import io.smallrye.mutiny.Uni;

public interface InitializeDatabaseSchemaMethod {
    Uni<Void> execute();
}
