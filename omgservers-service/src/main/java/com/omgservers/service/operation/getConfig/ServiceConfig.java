package com.omgservers.service.operation.getConfig;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import org.eclipse.microprofile.config.spi.Converter;

import java.net.URI;
import java.util.List;

@ConfigMapping(prefix = "omgservers")
public interface ServiceConfig {

    DefaultsConfig defaults();

    GeneratorConfig generator();

    IndexConfig index();

    JwtConfig jwt();

    BootstrapConfig bootstrap();

    ClientsConfig clients();

    DockerConfig docker();

    RuntimesConfig runtimes();

    BuilderConfig builder();

    RegistryConfig registry();

    interface DefaultsConfig {
        long indexId();

        long rootId();

        long poolId();

        long adminUserId();

        long supportUserId();

        long registryUserId();

        long builderUserId();

        long serviceUserId();
    }

    interface GeneratorConfig {
        long datacenterId();

        long instanceId();
    }

    interface IndexConfig {
        int shardCount();

        URI serverUri();
    }

    interface JwtConfig {
        String issuer();

        String x5c();
    }

    interface BootstrapRelayJobConfig {
        boolean enabled();

        String interval();
    }

    interface BootstrapSchedulerJobConfig {
        boolean enabled();

        String interval();
    }

    interface ClientsConfig {
        long tokenLifetime();

        long inactiveInterval();
    }

    interface DockerConfig {
        boolean tlsVerify();

        @WithConverter(UserHomeConverter.class)
        String certPath();
    }

    interface RuntimesConfig {
        long inactiveInterval();

        String dockerNetwork();

        URI serviceUri();

        int defaultCpuLimit();

        int defaultMemoryLimit();
    }

    interface BootstrapConfig {
        BootstrapSchemaConfig schema();

        BootstrapIndexConfig index();

        BootstrapDefaultUsersConfig defaultUsers();

        BootstrapUserPasswordConfig adminUser();

        BootstrapUserPasswordConfig supportUser();

        BootstrapUserPasswordConfig registryUser();

        BootstrapUserPasswordConfig builderUser();

        BootstrapUserPasswordConfig serviceUser();

        BootstrapRootConfig root();

        BootstrapDefaultPoolConfig defaultPool();

        BootstrapRelayJobConfig relayJob();

        BootstrapSchedulerJobConfig schedulerJob();
    }

    interface BootstrapSchemaConfig {
        boolean enabled();

        int concurrency();
    }

    interface BootstrapIndexConfig {
        boolean enabled();

        List<URI> servers();
    }

    interface BootstrapDefaultUsersConfig {
        boolean enabled();
    }

    interface BootstrapUserPasswordConfig {
        String password();
    }

    interface BootstrapRootConfig {
        boolean enabled();
    }

    interface BootstrapDefaultPoolConfig {
        boolean enabled();

        URI dockerUri();

        int cpuCount();

        int memorySize();

        int maxContainers();
    }

    interface BuilderConfig {
        URI uri();

        String userId();

        String userToken();
    }

    interface RegistryConfig {
        URI uri();
    }

    class UserHomeConverter implements Converter<String> {
        @Override
        public String convert(String path) throws IllegalArgumentException, NullPointerException {
            return path.replaceFirst("^~", System.getProperty("user.home"));
        }
    }
}
