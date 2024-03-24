package com.omgservers.service.module.client.impl.operation.getClientModuleClient;

import com.omgservers.service.exception.ClientSideExceptionMapper;
import com.omgservers.service.module.client.impl.service.webService.impl.api.ClientApi;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

@RegisterProvider(ClientSideExceptionMapper.class)
public interface ClientModuleClient extends ClientApi {
}
