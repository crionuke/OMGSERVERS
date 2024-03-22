package com.omgservers.tester.lobby;

import com.omgservers.model.message.MessageQualifierEnum;
import com.omgservers.tester.component.AdminApiTester;
import com.omgservers.tester.component.PlayerApiTester;
import com.omgservers.tester.operation.bootstrapTestClient.BootstrapTestClientOperation;
import com.omgservers.tester.operation.bootstrapTestVersion.BootstrapTestVersionOperation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@Slf4j
@QuarkusTest
public class LobbyInactiveClientDetectionIT extends Assertions {

    @Inject
    BootstrapTestVersionOperation bootstrapTestVersionOperation;

    @Inject
    BootstrapTestClientOperation bootstrapTestClientOperation;

    @Inject
    PlayerApiTester playerApiTester;

    @Inject
    AdminApiTester adminApiTester;

    @Test
    void lobbyInactiveClientDetectionIT() throws Exception {
        final var testVersion =
                bootstrapTestVersionOperation.bootstrapTestVersion("""
                                function handle_command(self, command)
                                end
                                """,
                        """
                                function handle_command(self, command)
                                end
                                """);

        Thread.sleep(10_000);

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

            Thread.sleep(10_000);

            // TODO: implement test
            //final var disconnectionMessage = playerApiTester.waitMessage(testClient,
            //        MessageQualifierEnum.DISCONNECTION_MESSAGE,
            //        Collections.singletonList(matchmakerAssignment.getId()));
            //
            //assertEquals(DisconnectionReasonEnum.CLIENT_INACTIVITY,
            //        ((DisconnectionReasonMessageBodyModel) disconnectionMessage.getBody()).getReason());

        } finally {
            adminApiTester.deleteTenant(testVersion.getTenantId());
        }
    }
}
