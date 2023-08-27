package com.omgservers.module.handler.impl.operation.createLuaRuntime.impl;

import com.omgservers.dto.tenant.GetStageInternalResponse;
import com.omgservers.dto.tenant.GetStageShardedRequest;
import com.omgservers.dto.version.GetBytecodeShardedRequest;
import com.omgservers.dto.version.GetBytecodeShardedResponse;
import com.omgservers.model.stage.StageModel;
import com.omgservers.model.version.VersionBytecodeModel;
import com.omgservers.module.handler.impl.operation.createLuaRuntime.CreateLuaRuntimeOperation;
import com.omgservers.module.handler.impl.operation.decodeLuaBytecode.DecodeLuaBytecodeOperation;
import com.omgservers.module.tenant.TenantModule;
import com.omgservers.module.version.VersionModule;
import com.omgservers.operation.createServerGlobals.CreateServerGlobalsOperation;
import com.omgservers.operation.createUserGlobals.CreateUserGlobalsOperation;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.luaj.vm2.LuaValue;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
class CreateLuaRuntimeOperationImpl implements CreateLuaRuntimeOperation {

    final VersionModule versionModule;
    final TenantModule tenantModule;

    final DecodeLuaBytecodeOperation decodeLuaBytecodeOperation;
    final CreateUserGlobalsOperation createUserGlobalsOperation;
    final CreateServerGlobalsOperation createServerGlobalsOperation;

    @Override
    public Uni<LuaRuntime> createLuaRuntime(final Long tenantId, final Long stageId) {
        if (tenantId == null) {
            throw new IllegalArgumentException("tenantId is null");
        }
        if (stageId == null) {
            throw new IllegalArgumentException("stageId is null");
        }

        return getStageVersion(tenantId, stageId)
                .flatMap(this::getBytecode)
                .map(versionBytecode -> {
                    final var base64Files = versionBytecode.getFiles();
                    log.info("Create lua runtime, countOfFiles={}", base64Files.size());
                    final var luaBytecode = decodeLuaBytecodeOperation.decodeLuaBytecode(base64Files);
                    // TODO: use user globals?
                    final var globals = createServerGlobalsOperation.createServerGlobals();
                    globals.finder = new LuaResourceFinder(luaBytecode);
                    final var luaRuntime = new LuaRuntime(globals);
                    // TODO: use param for main file
                    luaRuntime.getGlobals().get("dofile").call(LuaValue.valueOf("main.lua"));
                    return luaRuntime;
                })
                .invoke(luaRuntime -> log.info("Lua runtime was created, {}", luaRuntime));
    }

    Uni<Long> getStageVersion(final Long tenantId, final Long stageId) {
        final var request = new GetStageShardedRequest(tenantId, stageId);
        return tenantModule.getStageShardedService().getStage(request)
                .map(GetStageInternalResponse::getStage)
                .map(StageModel::getVersionId);
    }

    Uni<VersionBytecodeModel> getBytecode(final Long id) {
        final var request = new GetBytecodeShardedRequest(id);
        return versionModule.getVersionShardedService().getBytecode(request)
                .map(GetBytecodeShardedResponse::getBytecode);
    }
}
