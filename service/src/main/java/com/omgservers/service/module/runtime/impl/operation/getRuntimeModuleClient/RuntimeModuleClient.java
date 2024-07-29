package com.omgservers.service.module.runtime.impl.operation.getRuntimeModuleClient;

import com.omgservers.service.server.component.ServiceHeadersFactory;
import com.omgservers.service.exception.ClientSideExceptionMapper;
import com.omgservers.service.module.runtime.impl.service.webService.impl.api.RuntimeApi;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

@RegisterProvider(ClientSideExceptionMapper.class)
@RegisterClientHeaders(ServiceHeadersFactory.class)
public interface RuntimeModuleClient extends RuntimeApi {
}
