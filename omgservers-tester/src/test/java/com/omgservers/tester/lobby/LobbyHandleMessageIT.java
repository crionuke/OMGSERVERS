package com.omgservers.tester.lobby;

import com.omgservers.model.message.MessageQualifierEnum;
import com.omgservers.model.message.body.ServerOutgoingMessageBodyModel;
import com.omgservers.tester.component.PlayerApiTester;
import com.omgservers.tester.component.SupportApiTester;
import com.omgservers.tester.operation.bootstrapTestClient.BootstrapTestClientOperation;
import com.omgservers.tester.operation.bootstrapTestVersion.BootstrapTestVersionOperation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@Slf4j
@QuarkusTest
public class LobbyHandleMessageIT extends Assertions {

    @Inject
    BootstrapTestVersionOperation bootstrapTestVersionOperation;

    @Inject
    BootstrapTestClientOperation bootstrapTestClientOperation;

    @Inject
    PlayerApiTester playerApiTester;

    @Inject
    SupportApiTester supportApiTester;

    @Test
    void lobbyHandleMessage() throws Exception {
        final var testVersion = bootstrapTestVersionOperation.bootstrapTestVersion(
                """
                        function handle_command(self, command)
                            if command.qualifier == "handle_message" then
                                local var message = command.message
                                assert(message.text == "helloworld", "message.text is wrong")
                                return {
                                    {
                                        qualifier = "respond_client",
                                        client_id = command.client_id,
                                        message = {
                                            text = "message_was_handled"
                                        }
                                    }
                                }
                            end
                        end
                        """,
                """
                        function handle_command(self, command)
                        end
                        """);

        Thread.sleep(10000);

        try {
            final var testClient = bootstrapTestClientOperation.bootstrapTestClient(testVersion);

            final var welcomeMessage = playerApiTester.waitMessage(testClient,
                    MessageQualifierEnum.SERVER_WELCOME_MESSAGE);

            final var lobbyAssignment = playerApiTester.waitMessage(testClient,
                    MessageQualifierEnum.RUNTIME_ASSIGNMENT_MESSAGE,
                    Collections.singletonList(welcomeMessage.getId()));

            final var matchmakerAssignment = playerApiTester.waitMessage(testClient,
                    MessageQualifierEnum.MATCHMAKER_ASSIGNMENT_MESSAGE,
                    Collections.singletonList(lobbyAssignment.getId()));

            playerApiTester.sendMessage(testClient, new TestMessage("helloworld"));

            final var serverMessage = playerApiTester.waitMessage(testClient,
                    MessageQualifierEnum.SERVER_OUTGOING_MESSAGE,
                    Collections.singletonList(matchmakerAssignment.getId()));
            assertEquals("{text=message_was_handled}",
                    ((ServerOutgoingMessageBodyModel) serverMessage.getBody()).getMessage().toString());

        } finally {
            supportApiTester.deleteTenant(testVersion.getTenantId());
        }
    }

    @Data
    @AllArgsConstructor
    static class TestMessage {
        String text;
    }
}
