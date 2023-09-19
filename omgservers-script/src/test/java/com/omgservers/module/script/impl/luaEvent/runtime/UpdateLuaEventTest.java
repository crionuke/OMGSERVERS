package com.omgservers.module.script.impl.luaEvent.runtime;

import com.omgservers.module.script.TestLuaGlobals;
import com.omgservers.module.script.impl.operation.createLuaRuntimeContext.CreateLuaRuntimeContextOperation;
import com.omgservers.operation.generateId.GenerateIdOperation;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class UpdateLuaEventTest extends Assertions {
    private static final long TIMEOUT = 1L;

    @Inject
    TestLuaGlobals testLuaGlobals;

    @Inject
    CreateLuaRuntimeContextOperation createLuaRuntimeContextOperation;

    @Inject
    GenerateIdOperation generateIdOperation;

    @Test
    void LuaUpdateRuntimeCommandReceivedEvent() {
        final var luaGlobals = testLuaGlobals.createTestGlobalsForScript("""
                -- main.lua
                   
                function update(event, runtime)
                    print("event.id=" .. event.id)
                    print("event.time=" .. event.time)
                    print("runtime.matchmaker_id=" .. runtime.matchmaker_id)
                    print("runtime.match_id=" .. runtime.match_id)
                    print("runtime.runtime_id=" .. runtime.runtime_id)
                    -- assert
                    assert(event.id == "update", "event.id is wrong")
                    assert(event.time == 1695156707, "event.time is wrong")
                    assert(runtime.matchmaker_id == "27535430859688964", "runtime.matchmaker_id is wrong")
                    assert(runtime.match_id == "27535430859688965", "runtime.match_id is wrong")
                    assert(runtime.runtime_id == "27535430859688966", "runtime.runtime_id is wrong")
                end
                """);

        final var luaEvent = new UpdateLuaEvent(1695156707L);
        final var luaContext = createLuaRuntimeContextOperation.createLuaRuntimeContext(TIMEOUT,
                27535430859688964L,
                27535430859688965L,
                27535430859688966L);

        luaGlobals.handleEvent(luaEvent, luaContext);
    }

}