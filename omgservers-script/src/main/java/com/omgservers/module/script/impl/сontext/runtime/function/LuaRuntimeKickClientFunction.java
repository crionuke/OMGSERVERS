package com.omgservers.module.script.impl.сontext.runtime.function;

import com.omgservers.dto.runtime.DoKickClientRequest;
import com.omgservers.module.runtime.RuntimeModule;
import com.omgservers.module.script.impl.operation.handleLuaCallOperation.HandleLuaCallOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

@Slf4j
@AllArgsConstructor
public class LuaRuntimeKickClientFunction extends VarArgFunction {
    final RuntimeModule runtimeModule;

    final HandleLuaCallOperation handleLuaCallOperation;

    final Long runtimeId;

    @Override
    public Varargs invoke(Varargs args) {
        return handleLuaCallOperation.handleLuaCall(() -> {
            final var userId = Long.valueOf(args.arg(1).checkjstring());
            final var clientId = Long.valueOf(args.arg(2).checkjstring());

            final var doKickClientRequest = new DoKickClientRequest(runtimeId, userId, clientId);
            return runtimeModule.getDoService().doKickClient(doKickClientRequest)
                    .replaceWith(LuaValue.NIL);
        });
    }
}
