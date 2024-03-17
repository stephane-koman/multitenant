package io.skoman.multitenant.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

import static io.skoman.multitenant.constants.ITokenConstant.LOGOUT_URL_SUFFIX;

@Component
@RequiredArgsConstructor
public class KeycloakLogoutHandler implements LogoutHandler {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    private static final Logger logger = LoggerFactory.getLogger(KeycloakLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        String refreshToken = request.getHeader(OAuth2Constants.REFRESH_TOKEN);
        logoutFromKeycloak((Jwt) auth.getPrincipal(), refreshToken);
    }

    private void logoutFromKeycloak(Jwt user, String refreshToken) {
        String endSessionEndpoint = user.getIssuer() + LOGOUT_URL_SUFFIX;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(OAuth2Constants.CLIENT_ID, clientId);
        map.add(OAuth2Constants.REFRESH_TOKEN, refreshToken);

        WebClient webClient = WebClient.create();

        WebClient.ResponseSpec response = webClient.post()
                .uri(endSessionEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(
                        BodyInserters.fromFormData(map)
                )
                .retrieve();

        if (Objects.requireNonNull(response.toBodilessEntity().block()).getStatusCode().is2xxSuccessful()) {
            logger.info("Successfully logged out from Keycloak");
        } else {
            logger.error("Could not propagate logout to Keycloak");
        }
    }
}
