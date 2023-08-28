package com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.doRuntimeUpdate;

import com.omgservers.dto.runtime.DoRuntimeUpdateShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeCommandShardedRequest;
import com.omgservers.dto.runtime.SyncRuntimeShardedRequest;
import com.omgservers.factory.RuntimeCommandModelFactory;
import com.omgservers.factory.RuntimeModelFactory;
import com.omgservers.model.runtime.RuntimeConfigModel;
import com.omgservers.model.runtime.RuntimeTypeEnum;
import com.omgservers.model.runtimeCommand.RuntimeCommandStatusEnum;
import com.omgservers.model.runtimeCommand.body.InitRuntimeCommandBodyModel;
import com.omgservers.model.runtimeCommand.body.StopRuntimeCommandBodyModel;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncRuntime.SyncRuntimeMethod;
import com.omgservers.module.runtime.impl.service.runtimeShardedService.impl.method.syncRuntimeCommand.SyncRuntimeCommandMethod;
import com.omgservers.operation.generateId.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class DoRuntimeUpdateMethodTest extends Assertions {
    static private final long TIMEOUT = 1L;

    @Inject
    DoRuntimeUpdateMethod doRuntimeUpdateMethod;

    @Inject
    SyncRuntimeMethod syncRuntimeMethod;

    @Inject
    SyncRuntimeCommandMethod syncRuntimeCommandMethod;

    @Inject
    RuntimeModelFactory runtimeModelFactory;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Inject
    RuntimeCommandModelFactory runtimeCommandModelFactory;

    @Test
    void doRuntimeUpdateTest() {
        final var runtime = runtimeModelFactory.create(matchmakerId(), matchId(), RuntimeTypeEnum.EMBEDDED_LUA, RuntimeConfigModel.create());
        final var syncRuntimeShardedRequest = new SyncRuntimeShardedRequest(runtime);
        syncRuntimeMethod.syncRuntime(TIMEOUT, syncRuntimeShardedRequest);

        final var runtimeCommand1 = runtimeCommandModelFactory.create(runtime.getId(), new InitRuntimeCommandBodyModel());
        final var syncRuntimeCommandShardedRequest1 = new SyncRuntimeCommandShardedRequest(runtimeCommand1);
        syncRuntimeCommandMethod.syncRuntimeCommand(TIMEOUT, syncRuntimeCommandShardedRequest1);

        final var runtimeCommand2 = runtimeCommandModelFactory.create(runtime.getId(), new StopRuntimeCommandBodyModel());
        final var syncRuntimeCommandShardedRequest2 = new SyncRuntimeCommandShardedRequest(runtimeCommand2);
        syncRuntimeCommandMethod.syncRuntimeCommand(TIMEOUT, syncRuntimeCommandShardedRequest2);

        final var doRuntimeUpdateShardedRequest = new DoRuntimeUpdateShardedRequest(runtime.getId());
        final var response = doRuntimeUpdateMethod.doRuntimeUpdate(TIMEOUT, doRuntimeUpdateShardedRequest);
        assertEquals(2, response.getHandledCommands());
        final var affectedCommand1 = response.getExtendedResponse().getAffectedCommands().get(0);
        assertEquals(runtimeCommand1.getId(), affectedCommand1.getId());
        assertEquals(RuntimeCommandStatusEnum.PROCESSED, affectedCommand1.getStatus());
        assertEquals(1, affectedCommand1.getStep());
        final var affectedCommand2 = response.getExtendedResponse().getAffectedCommands().get(1);
        assertEquals(runtimeCommand2.getId(), affectedCommand2.getId());
        assertEquals(RuntimeCommandStatusEnum.PROCESSED, affectedCommand2.getStatus());
        assertEquals(1, affectedCommand2.getStep());
    }

    Long matchmakerId() {
        return generateIdOperation.generateId();
    }

    Long matchId() {
        return generateIdOperation.generateId();
    }
}