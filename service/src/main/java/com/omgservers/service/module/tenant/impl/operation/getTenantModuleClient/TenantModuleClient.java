package com.omgservers.service.module.tenant.impl.operation.getTenantModuleClient;

import com.omgservers.service.server.component.ServiceHeadersFactory;
import com.omgservers.service.exception.ClientSideExceptionMapper;
import com.omgservers.service.module.tenant.impl.service.webService.impl.api.TenantApi;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

@RegisterProvider(ClientSideExceptionMapper.class)
@RegisterClientHeaders(ServiceHeadersFactory.class)
public interface TenantModuleClient extends TenantApi {
}
