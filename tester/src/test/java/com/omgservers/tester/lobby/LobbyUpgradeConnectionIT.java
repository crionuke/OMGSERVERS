package com.omgservers.tester.lobby;

import com.omgservers.model.message.MessageQualifierEnum;
import com.omgservers.model.message.body.ConnectionUpgradeMessageBodyModel;
import com.omgservers.model.message.body.ConnectionUpgradeQualifierEnum;
import com.omgservers.tester.BaseTestClass;
import com.omgservers.tester.component.PlayerApiTester;
import com.omgservers.tester.component.SupportApiTester;
import com.omgservers.tester.operation.bootstrapTestClient.BootstrapTestClientOperation;
import com.omgservers.tester.operation.bootstrapTestVersion.BootstrapTestVersionOperation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@Slf4j
@QuarkusTest
public class LobbyUpgradeConnectionIT extends BaseTestClass {

    @Inject
    BootstrapTestVersionOperation bootstrapTestVersionOperation;

    @Inject
    BootstrapTestClientOperation bootstrapTestClientOperation;

    @Inject
    PlayerApiTester playerApiTester;

    @Inject
    SupportApiTester supportApiTester;

    @Test
    void lobbyUpgradeConnectionIT() throws Exception {
        final var testVersion = bootstrapTestVersionOperation.bootstrapTestVersion("""
                local omgserver = require("omgserver")
                omgserver:enter_loop({
                    handle = function(self, command_qualifier, command_body)
                        local runtime_qualifier = omgserver.qualifier
                        
                        if runtime_qualifier == "LOBBY" then
                            if command_qualifier == "ADD_CLIENT" then
                                return {
                                    {
                                        qualifier = "UPGRADE_CONNECTION",
                                        body = {
                                            client_id = command_body.client_id,
                                            protocol = "WEBSOCKET",
                                        }
                                    }
                                }
                            end
                        elseif runtime_qualifier == "MATCH" then
                        end
                    end,
                })
                """);

        Thread.sleep(21_000);

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
            final var connectionUpgrade = playerApiTester.waitMessage(testClient,
                    MessageQualifierEnum.CONNECTION_UPGRADE_MESSAGE,
                    Collections.singletonList(matchmakerAssignment.getId()));

            assertEquals(testClient.getClientId(),
                    ((ConnectionUpgradeMessageBodyModel) connectionUpgrade.getBody()).getClientId());
            assertEquals(ConnectionUpgradeQualifierEnum.WEBSOCKET,
                    ((ConnectionUpgradeMessageBodyModel) connectionUpgrade.getBody()).getProtocol());
            assertNotNull(((ConnectionUpgradeMessageBodyModel) connectionUpgrade.getBody()).getWebSocketConfig());
            assertNotNull(((ConnectionUpgradeMessageBodyModel) connectionUpgrade.getBody()).getWebSocketConfig()
                    .getWsToken());
        } finally {
            supportApiTester.deleteTenant(testVersion.getSupportToken(), testVersion.getTenantId());
        }
    }
}
