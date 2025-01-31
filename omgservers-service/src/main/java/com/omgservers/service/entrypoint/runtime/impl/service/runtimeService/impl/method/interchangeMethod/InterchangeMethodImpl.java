package com.omgservers.service.entrypoint.runtime.impl.service.runtimeService.impl.method.interchangeMethod;

import com.omgservers.schema.entrypoint.runtime.InterchangeRuntimeRequest;
import com.omgservers.schema.entrypoint.runtime.InterchangeRuntimeResponse;
import com.omgservers.schema.module.runtime.InterchangeRequest;
import com.omgservers.schema.module.runtime.InterchangeResponse;
import com.omgservers.service.shard.runtime.RuntimeShard;
import com.omgservers.service.shard.user.UserShard;
import com.omgservers.service.security.SecurityAttributesEnum;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class InterchangeMethodImpl implements InterchangeMethod {

    final RuntimeShard runtimeShard;
    final UserShard userShard;

    final SecurityIdentity securityIdentity;

    @Override
    public Uni<InterchangeRuntimeResponse> execute(final InterchangeRuntimeRequest request) {
        log.debug("Requested, {}", request);

        final var runtimeId = securityIdentity
                .<Long>getAttribute(SecurityAttributesEnum.RUNTIME_ID.getAttributeName());

        final var outgoingCommands = request.getOutgoingCommands();
        final var consumedCommands = request.getConsumedCommands();

        final var interchangeRequest = new InterchangeRequest(runtimeId, outgoingCommands, consumedCommands);
        return runtimeShard.getService().execute(interchangeRequest)
                .map(InterchangeResponse::getIncomingCommands)
                .map(InterchangeRuntimeResponse::new);
    }
}
