package com.omgservers.module.script.impl.operation.mapScriptRequest.mapper;

import com.omgservers.model.scriptRequest.ScriptRequestModel;
import com.omgservers.model.scriptRequest.ScriptRequestQualifierEnum;
import com.omgservers.model.scriptRequest.arguments.UpdateRuntimeScriptRequestArgumentsModel;
import com.omgservers.module.script.impl.luaRequest.LuaRequest;
import com.omgservers.module.script.impl.luaRequest.UpdateRuntimeLuaRequest;
import com.omgservers.module.script.impl.operation.mapScriptRequest.ScriptRequestMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UpdateRuntimeScriptRequestMapper implements ScriptRequestMapper {

    @Override
    public ScriptRequestQualifierEnum getQualifier() {
        return ScriptRequestQualifierEnum.UPDATE_RUNTIME;
    }

    @Override
    public LuaRequest map(ScriptRequestModel scriptRequest) {
        final var body = (UpdateRuntimeScriptRequestArgumentsModel) scriptRequest.getArguments();
        return UpdateRuntimeLuaRequest.builder()
                .time(body.getTime())
                .build();
    }
}
