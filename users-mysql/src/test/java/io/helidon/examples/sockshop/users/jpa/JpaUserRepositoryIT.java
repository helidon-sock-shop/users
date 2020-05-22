package io.helidon.examples.sockshop.users.jpa;

import io.helidon.examples.sockshop.users.UserRepository;
import io.helidon.examples.sockshop.users.UserRepositoryTest;
import io.helidon.microprofile.server.Server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Integration tests for {@link JpaUserRepository}.
 */
public class JpaUserRepositoryIT extends UserRepositoryTest {
    private static Server SERVER;

    /**
     * This will start the application on ephemeral port to avoid port conflicts.
     * We can discover the actual port by calling {@link io.helidon.microprofile.server.Server#port()} method afterwards.
     */
    @BeforeAll
    static void startServer() {
        // disable global tracing so we can start server in multiple test suites
        System.setProperty("tracing.global", "false");
        SERVER = Server.builder().port(0).build().start();
    }

    /**
     * Stop the server, as we cannot have multiple servers started at the same time.
     */
    @AfterAll

    static void stopServer() {
        SERVER.stop();
    }

    @Override
    protected UserRepository getUserRepository() {
        return SERVER.cdiContainer().select(UserRepository.class).get();
    }
}