package com.omgservers.service.operation.getIdByUser;

import io.smallrye.mutiny.Uni;

public interface GetIdByUserOperation {
    Uni<Long> execute(String user);
}
