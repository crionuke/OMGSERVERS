package com.omgservers.application.module.luaModule.impl.service.luaRuntimeHelpService.response;

import com.omgservers.application.module.luaModule.model.LuaBytecodeModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompileSourceCodeHelpResponse {

    List<LuaBytecodeModel> files;
}
