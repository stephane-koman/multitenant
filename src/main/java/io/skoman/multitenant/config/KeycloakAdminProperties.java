package io.skoman.multitenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.keycloak")
public record KeycloakAdminProperties(
        String realm, String serverUrl,
        String clientId, String username,
        String password
) {
}
