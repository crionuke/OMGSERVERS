package com.omgservers.service.module.root.impl.service.rootService.testInterface;

import com.omgservers.model.dto.root.root.DeleteRootRequest;
import com.omgservers.model.dto.root.root.DeleteRootResponse;
import com.omgservers.model.dto.root.root.GetRootRequest;
import com.omgservers.model.dto.root.root.GetRootResponse;
import com.omgservers.model.dto.root.root.SyncRootRequest;
import com.omgservers.model.dto.root.root.SyncRootResponse;
import com.omgservers.service.module.root.impl.service.rootService.RootService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class RootServiceTestInterface {
    private static final long TIMEOUT = 1L;

    final RootService rootService;

    public GetRootResponse getRoot(final GetRootRequest request) {
        return rootService.getRoot(request)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }

    public SyncRootResponse syncRoot(final SyncRootRequest request) {
        return rootService.syncRoot(request)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }

    public DeleteRootResponse deleteRoot(final DeleteRootRequest request) {
        return rootService.deleteRoot(request)
                .await().atMost(Duration.ofSeconds(TIMEOUT));
    }
}
