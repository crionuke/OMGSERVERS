package com.omgservers.module.gateway.impl.operation.getGatewayModuleClient;

import com.omgservers.module.gateway.impl.service.gatewayService.impl.serviceApi.GatewayServiceApi;
import com.omgservers.security.ServiceAccountClientHeadersFactory;
import com.omgservers.exception.ClientSideExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

@RegisterProvider(ClientSideExceptionMapper.class)
@RegisterClientHeaders(ServiceAccountClientHeadersFactory.class)
public interface GatewayModuleClient extends GatewayServiceApi {
}
