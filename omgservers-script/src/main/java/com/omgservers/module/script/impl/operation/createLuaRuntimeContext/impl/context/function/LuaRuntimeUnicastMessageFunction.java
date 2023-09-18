package com.omgservers.module.script.impl.operation.createLuaRuntimeContext.impl.context.function;

import com.omgservers.dto.runtime.DoUnicastMessageRequest;
import com.omgservers.module.runtime.RuntimeModule;
import com.omgservers.module.script.impl.operation.handleLuaCallOperation.HandleLuaCallOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

@Slf4j
@AllArgsConstructor
public class LuaRuntimeUnicastMessageFunction extends VarArgFunction {
    final RuntimeModule runtimeModule;

    final HandleLuaCallOperation handleLuaCallOperation;

    final Long runtimeId;

    @Override
    public Varargs invoke(Varargs args) {
        return handleLuaCallOperation.handleLuaCall(() -> {
            final var clientId = Long.valueOf(args.arg(1).checkjstring());
            final var message = args.arg(2).checkjstring();

            final var doUnicastMessageRequest = new DoUnicastMessageRequest(runtimeId, clientId, message);
            return runtimeModule.getDoService().doUnicastMessage(doUnicastMessageRequest)
                    .replaceWith(LuaValue.NIL);
        });
    }
}
