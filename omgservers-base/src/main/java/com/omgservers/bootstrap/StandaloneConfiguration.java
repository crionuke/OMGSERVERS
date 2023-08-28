package com.omgservers.bootstrap;

import com.omgservers.dto.internal.SyncIndexRequest;
import com.omgservers.dto.internal.SyncServiceAccountRequest;
import com.omgservers.model.index.IndexConfigModel;
import com.omgservers.module.internal.InternalModule;
import com.omgservers.factory.IndexModelFactory;
import com.omgservers.factory.ServiceAccountModelFactory;
import com.omgservers.operation.getConfig.GetConfigOperation;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Startup
@ApplicationScoped
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class StandaloneConfiguration {

    final InternalModule internalModule;

    final GetConfigOperation getConfigOperation;

    final IndexModelFactory indexModelFactory;
    final ServiceAccountModelFactory serviceAccountModelFactory;

    @PostConstruct
    void postConstruct() {
        if (getConfigOperation.getConfig().standalone()) {
            Uni.createFrom().voidItem()
                    .flatMap(voidItem -> syncIndex())
                    .flatMap(voidItem -> syncServiceAccount())
                    .invoke(voidItem -> log.info("StandaloneConfiguration configuration was created"))
                    .await().indefinitely();
        } else {
            log.warn("StandaloneConfiguration of standalone configuration was skipped");
        }
    }

    Uni<Void> syncIndex() {
        final var indexName = getConfigOperation.getConfig().indexName();
        final var serverUri = getConfigOperation.getConfig().serverUri();
        final var indexConfig = IndexConfigModel.create(Collections.singletonList(serverUri));
        final var indexModel = indexModelFactory.create(indexName, indexConfig);
        final var request = new SyncIndexRequest(indexModel);
        return internalModule.getIndexService().syncIndex(request);
    }

    Uni<Void> syncServiceAccount() {
        final var serviceUsername = getConfigOperation.getConfig().serviceUsername();
        final var servicePassword = getConfigOperation.getConfig().servicePassword();
        final var passwordHash = BcryptUtil.bcryptHash(servicePassword);
        final var serviceAccountModel = serviceAccountModelFactory.create(serviceUsername, passwordHash);
        final var request = new SyncServiceAccountRequest(serviceAccountModel);
        return internalModule.getServiceAccountService().syncServiceAccount(request);
    }
}
