package ru.phoenix.common.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter loginSuccess;
    private final Counter loginFailed;

    private final Counter usersCreated;
    private final Counter usersPromoted;
    private final Counter usersDemoted;

    private final Counter productsCreated;
    private final Counter productsUpdated;
    private final Counter productsDeleted;
    private final Counter productsViewed;

    private final Counter applicationErrors;

    public MetricsService(MeterRegistry registry) {

        loginSuccess = Counter.builder("auth_login_success_total")
                .description("Successful login attempts")
                .register(registry);

        loginFailed = Counter.builder("auth_login_failed_total")
                .description("Failed login attempts")
                .register(registry);

        usersCreated = Counter.builder("users_created_total")
                .description("Users created")
                .register(registry);

        usersPromoted = Counter.builder("users_promoted_total")
                .description("Users promoted to admin")
                .register(registry);

        usersDemoted = Counter.builder("users_demoted_total")
                .description("Admins demoted")
                .register(registry);

        productsCreated = Counter.builder("products_created_total")
                .description("Products created")
                .register(registry);

        productsUpdated = Counter.builder("products_updated_total")
                .description("Products updated")
                .register(registry);

        productsDeleted = Counter.builder("products_deleted_total")
                .description("Products deleted")
                .register(registry);

        productsViewed = Counter.builder("products_viewed_total")
                .description("Products viewed")
                .register(registry);

        applicationErrors = Counter.builder("application_errors_total")
                .description("Application errors")
                .register(registry);
    }

    public void loginSuccess() { loginSuccess.increment(); }
    public void loginFailed() { loginFailed.increment(); }

    public void userCreated() { usersCreated.increment(); }
    public void userPromoted() { usersPromoted.increment(); }
    public void userDemoted() { usersDemoted.increment(); }

    public void productCreated() { productsCreated.increment(); }
    public void productUpdated() { productsUpdated.increment(); }
    public void productDeleted() { productsDeleted.increment(); }
    public void productViewed() { productsViewed.increment(); }

    public void applicationError() { applicationErrors.increment(); }
}