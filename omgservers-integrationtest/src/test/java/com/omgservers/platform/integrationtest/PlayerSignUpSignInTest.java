package com.omgservers.platform.integrationtest;

import com.omgservers.platforms.integrationtest.operations.bootstrapVersionOperation.BootstrapVersionOperation;
import com.omgservers.platforms.integrationtest.serviceClient.ServiceClientFactory;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@Slf4j
@QuarkusTest
public class PlayerSignUpSignInTest extends Assertions {

    @Inject
    BootstrapVersionOperation bootstrapVersionOperation;

    @Inject
    ServiceClientFactory serviceClientFactory;

    @Test
    void givenVersion_whenSignUpSignIn() throws Exception {
        final var version = bootstrapVersionOperation.bootstrap("""
                function player_signed_up(event, player)
                    player.respond("player_signed_up")
                end
                                
                function player_signed_in(event, player)
                    player.respond("player_signed_in")
                end
                                
                print("version was initialized")
                """);

        final var client = serviceClientFactory.create();
        client.signUp(version);
        var event1 = client.consumeEventMessage();
        assertEquals("player_signed_up", event1.getEvent().toString());
        Thread.sleep(1000);

        client.reconnect();
        client.signIn(version);
        var event2 = client.consumeEventMessage();
        assertEquals("player_signed_in", event2.getEvent().toString());
        client.close();

        Thread.sleep(1000);

        log.info("Finished");
    }
}
