package io.skoman.multitenant.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloakAdminConfig {

    @Autowired
    private KeycloakAdminProperties kcaProperties;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(kcaProperties.serverUrl())
                .realm(kcaProperties.realm())
                .clientId(kcaProperties.clientId())
                .grantType(OAuth2Constants.PASSWORD)
                .username(kcaProperties.username())
                .password(kcaProperties.password())
                .build();
    }
}
