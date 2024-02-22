package com.omgservers.service.module.runtime;

import com.omgservers.service.module.runtime.impl.service.doService.DoService;
import com.omgservers.service.module.runtime.impl.service.runtimeService.RuntimeService;

public interface RuntimeModule {

    RuntimeService getRuntimeService();

    DoService getDoService();
}
