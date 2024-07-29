package com.omgservers.service.entrypoint.worker.impl.service.workerService.impl;

import com.omgservers.schema.entrypoint.worker.CreateTokenWorkerRequest;
import com.omgservers.schema.entrypoint.worker.CreateTokenWorkerResponse;
import com.omgservers.schema.entrypoint.worker.GetVersionWorkerRequest;
import com.omgservers.schema.entrypoint.worker.GetVersionWorkerResponse;
import com.omgservers.schema.entrypoint.worker.InterchangeWorkerRequest;
import com.omgservers.schema.entrypoint.worker.InterchangeWorkerResponse;
import com.omgservers.service.entrypoint.worker.impl.service.workerService.WorkerService;
import com.omgservers.service.entrypoint.worker.impl.service.workerService.impl.method.createToken.CreateTokenMethod;
import com.omgservers.service.entrypoint.worker.impl.service.workerService.impl.method.getVersion.GetVersionMethod;
import com.omgservers.service.entrypoint.worker.impl.service.workerService.impl.method.interchangeMethod.InterchangeMethod;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class WorkerServiceImpl implements WorkerService {

    final InterchangeMethod interchangeMethod;
    final CreateTokenMethod createTokenMethod;
    final GetVersionMethod getVersionMethod;

    @Override
    public Uni<CreateTokenWorkerResponse> createToken(@Valid final CreateTokenWorkerRequest request) {
        return createTokenMethod.createToken(request);
    }

    @Override
    public Uni<GetVersionWorkerResponse> getVersion(@Valid final GetVersionWorkerRequest request) {
        return getVersionMethod.getVersion(request);
    }

    @Override
    public Uni<InterchangeWorkerResponse> interchange(@Valid final InterchangeWorkerRequest request) {
        return interchangeMethod.interchange(request);
    }
}
