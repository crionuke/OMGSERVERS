package com.omgservers.service.operation;

import com.omgservers.BaseTestClass;
import com.omgservers.service.operation.calculateShard.CalculateShardOperation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class CalculateShardOperationTest extends BaseTestClass {

    @Inject
    CalculateShardOperation calculateShardOperation;

    @Test
    void givenSingleHashKey_whenCalculateShard() {
        final var indexShardCount = 32768;
        final var shard = calculateShardOperation.calculateShard(indexShardCount, "uuid");
        assertNotNull(shard);
        assertTrue(shard >= 0 && shard < indexShardCount);
    }

    @Test
    void givenCompositeHashKey_whenCalculateShard() {
        final var indexShardCount = 32768;
        final var shard = calculateShardOperation.calculateShard(indexShardCount, "db/system", "username");
        assertNotNull(shard);
        assertTrue(shard >= 0 && shard < indexShardCount);
    }
}