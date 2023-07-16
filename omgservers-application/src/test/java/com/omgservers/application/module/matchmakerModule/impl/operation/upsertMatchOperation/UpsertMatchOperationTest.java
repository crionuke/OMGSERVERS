package com.omgservers.application.module.matchmakerModule.impl.operation.upsertMatchOperation;

import com.omgservers.application.module.matchmakerModule.impl.operation.insertMatchmakerOperation.InsertMatchmakerOperation;
import com.omgservers.application.module.matchmakerModule.model.match.MatchConfigModel;
import com.omgservers.application.module.matchmakerModule.model.match.MatchModel;
import com.omgservers.application.module.matchmakerModule.model.matchmaker.MatchmakerModel;
import com.omgservers.application.module.versionModule.model.VersionGroupModel;
import com.omgservers.application.module.versionModule.model.VersionModeModel;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@QuarkusTest
class UpsertMatchOperationTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    UpsertMatchOperation upsertMatchOperation;

    @Inject
    InsertMatchmakerOperation insertMatchmakerOperation;

    @Inject
    PgPool pgPool;

    @Test
    void givenConfig_whenUpsertMatch_thenInserted() {
        final var shard = 0;
        final var matchmaker = MatchmakerModel.create(tenantUuid(), stageUuid());
        insertMatchmakerOperation.insertMatchmaker(TIMEOUT, pgPool, shard, matchmaker);

        final var modeConfig = VersionModeModel.create(modeName(), 4, 8, new ArrayList<>() {{
            add(VersionGroupModel.create("red", 1, 4));
            add(VersionGroupModel.create("blue", 1, 4));
        }});
        final var matchConfig = MatchConfigModel.create(tenantUuid(),
                stageUuid(),
                versionUuid(),
                modeConfig);
        final var match = MatchModel.create(matchmaker.getUuid(), matchConfig);
        assertTrue(upsertMatchOperation.upsertMatch(TIMEOUT, pgPool, shard, match));
    }

    @Test
    void givenMatch_whenUpsertMatch_thenUpdated() {
        final var shard = 0;
        final var matchmaker = MatchmakerModel.create(tenantUuid(), stageUuid());
        insertMatchmakerOperation.insertMatchmaker(TIMEOUT, pgPool, shard, matchmaker);

        final var modeConfig = VersionModeModel.create(modeName(), 4, 8, new ArrayList<>() {{
            add(VersionGroupModel.create("red", 1, 4));
            add(VersionGroupModel.create("blue", 1, 4));
        }});
        final var matchConfig = MatchConfigModel.create(tenantUuid(),
                stageUuid(),
                versionUuid(),
                modeConfig);
        final var match = MatchModel.create(matchmaker.getUuid(), matchConfig);
        upsertMatchOperation.upsertMatch(TIMEOUT, pgPool, shard, match);

        assertFalse(upsertMatchOperation.upsertMatch(TIMEOUT, pgPool, shard, match));
    }

    UUID tenantUuid() {
        return UUID.randomUUID();
    }

    UUID stageUuid() {
        return UUID.randomUUID();
    }

    UUID versionUuid() {
        return UUID.randomUUID();
    }

    String modeName() {
        return "mode-" + UUID.randomUUID();
    }
}