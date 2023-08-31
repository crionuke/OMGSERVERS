package com.omgservers;

import com.omgservers.utils.operation.BootstrapVersionOperation;
import com.omgservers.utils.testClient.TestClientFactory;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

@Slf4j
@QuarkusTest
public class PlayerSetGetAttributeTest extends Assertions {

    @TestHTTPResource("/omgservers/gateway")
    URI uri;

    @Inject
    BootstrapVersionOperation bootstrapVersionOperation;

    @Inject
    TestClientFactory testClientFactory;

    @Test
    void playerSetGetAttributeTest() throws Exception {
        final var version = bootstrapVersionOperation.bootstrapVersion("""
                function player_signed_up(event, player)
                    player.set_attribute("a1", "1")
                    player.set_attribute("a2", "2")
                    player.set_attribute("a3", "3")
                end

                function player_signed_in(event, player)
                    local a1 = player.get_attribute("a1")
                    print("a1:", a1)
                    player.respond(a1)
                    local a2 = player.get_attribute("a2")
                    print("a2:", a2)
                    player.respond(a2)
                    local a3 = player.get_attribute("a3")
                    print("a3:", a3)
                    player.respond(a3)
                end

                print("version was initialized")
                """);

        final var client = testClientFactory.create(uri);
        client.signUp(version);

        client.reconnect();
        client.signIn(version);
        var message1 = client.consumeEventMessage();
        assertEquals("1", message1.getEvent().toString());
        var message2 = client.consumeEventMessage();
        assertEquals("2", message2.getEvent().toString());
        var message3 = client.consumeEventMessage();
        assertEquals("3", message3.getEvent().toString());

        client.close();
    }
}
