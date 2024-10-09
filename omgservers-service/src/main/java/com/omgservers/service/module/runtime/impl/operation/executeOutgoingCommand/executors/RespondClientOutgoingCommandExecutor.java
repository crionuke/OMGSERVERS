package com.omgservers.service.module.runtime.impl.operation.executeOutgoingCommand.executors;

import com.omgservers.schema.model.clientMessage.ClientMessageModel;
import com.omgservers.schema.module.client.SyncClientMessageRequest;
import com.omgservers.schema.module.client.SyncClientMessageResponse;
import com.omgservers.schema.model.message.MessageQualifierEnum;
import com.omgservers.schema.model.message.body.ServerOutgoingMessageBodyModel;
import com.omgservers.schema.model.outgoingCommand.OutgoingCommandModel;
import com.omgservers.schema.model.outgoingCommand.OutgoingCommandQualifierEnum;
import com.omgservers.schema.model.outgoingCommand.body.RespondClientOutgoingCommandBodyModel;
import com.omgservers.schema.model.exception.ExceptionQualifierEnum;
import com.omgservers.service.exception.ServerSideBadRequestException;
import com.omgservers.service.factory.client.ClientMessageModelFactory;
import com.omgservers.service.factory.client.MessageModelFactory;
import com.omgservers.service.module.client.ClientModule;
import com.omgservers.service.module.runtime.impl.operation.executeOutgoingCommand.OutgoingCommandExecutor;
import com.omgservers.service.module.runtime.impl.operation.runtimeAssignment.hasRuntimeAssignment.HasRuntimeAssignmentOperation;
import com.omgservers.service.module.user.UserModule;
import com.omgservers.service.operation.checkShard.CheckShardOperation;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RespondClientOutgoingCommandExecutor implements OutgoingCommandExecutor {

    final ClientModule clientModule;
    final UserModule userModule;

    final HasRuntimeAssignmentOperation hasRuntimeAssignmentOperation;
    final CheckShardOperation checkShardOperation;

    final ClientMessageModelFactory clientMessageModelFactory;
    final MessageModelFactory messageModelFactory;
    final PgPool pgPool;

    @Override
    public OutgoingCommandQualifierEnum getQualifier() {
        return OutgoingCommandQualifierEnum.RESPOND_CLIENT;
    }

    @Override
    public Uni<Void> execute(final Long runtimeId, final OutgoingCommandModel outgoingCommand) {
        log.debug("Execute respond client outgoing command, outgoingCommand={}", outgoingCommand);

        final var commandBody = (RespondClientOutgoingCommandBodyModel) outgoingCommand.getBody();
        final var clientId = commandBody.getClientId();
        final var message = commandBody.getMessage();

        return checkShardOperation.checkShard(runtimeId.toString())
                .flatMap(shardModel -> pgPool.withTransaction(
                        sqlConnection -> hasRuntimeAssignmentOperation.hasRuntimeAssignment(
                                        sqlConnection,
                                        shardModel.shard(),
                                        runtimeId,
                                        clientId)
                                .flatMap(has -> {
                                    if (has) {
                                        return syncClientMessage(clientId, message)
                                                .replaceWithVoid();
                                    } else {
                                        throw new ServerSideBadRequestException(ExceptionQualifierEnum.PARENT_NOT_FOUND,
                                                String.format(
                                                        "runtime assignment was not found, runtimeId=%s, clientId=%s",
                                                        runtimeId, clientId));
                                    }
                                }))
                );
    }

    Uni<Boolean> syncClientMessage(final Long clientId,
                                   final Object message) {
        final var messageBody = new ServerOutgoingMessageBodyModel(message);
        final var clientMessage = clientMessageModelFactory.create(clientId,
                MessageQualifierEnum.SERVER_OUTGOING_MESSAGE,
                messageBody);
        return syncClientMessage(clientMessage);
    }

    Uni<Boolean> syncClientMessage(final ClientMessageModel clientMessage) {
        final var request = new SyncClientMessageRequest(clientMessage);
        return clientModule.getService().syncClientMessage(request)
                .map(SyncClientMessageResponse::getCreated);
    }
}
