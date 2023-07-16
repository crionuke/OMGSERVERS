package com.omgservers.application.module.userModule.impl.service.tokenInternalService.impl.method.createTokenMethod;

import com.omgservers.application.operation.checkShardOperation.CheckShardOperation;
import com.omgservers.application.module.userModule.impl.operation.validateCredentialsOperation.ValidateCredentialsOperation;
import com.omgservers.application.module.userModule.impl.operation.insertTokenOperation.InsertTokenOperation;
import com.omgservers.application.module.userModule.impl.operation.selectUserOperation.SelectUserOperation;
import com.omgservers.application.module.userModule.impl.service.tokenInternalService.request.CreateTokenInternalRequest;
import com.omgservers.application.module.userModule.impl.service.tokenInternalService.response.CreateTokenInternalResponse;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class CreateTokenMethodImpl implements CreateTokenMethod {

    final CheckShardOperation checkShardOperation;
    final SelectUserOperation selectUserOperation;
    final ValidateCredentialsOperation validateCredentialsOperation;
    final InsertTokenOperation insertTokenOperation;
    final PgPool pgPool;

    @Override
    public Uni<CreateTokenInternalResponse> createToken(final CreateTokenInternalRequest request) {
        CreateTokenInternalRequest.validate(request);

        final var userUuid = request.getUser();
        return checkShardOperation.checkShard(request.getRequestShardKey())
                .flatMap(shardModel -> {
                    final var shard = shardModel.shard();
                    final var password = request.getPassword();
                    return pgPool.withTransaction(sqlConnection -> selectUserOperation
                            .selectUser(sqlConnection, shard, userUuid)
                            .flatMap(user -> validateCredentialsOperation.validateCredentials(user, password))
                            .flatMap(user -> insertTokenOperation.insertToken(sqlConnection, shard, user)));
                })
                .map(tokenContainer -> new CreateTokenInternalResponse(tokenContainer.getTokenObject(),
                        tokenContainer.getRawToken(),
                        tokenContainer.getLifetime()));
    }
}
