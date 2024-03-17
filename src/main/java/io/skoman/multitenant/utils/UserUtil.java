package io.skoman.multitenant.utils;

import io.skoman.multitenant.dtos.UserDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

import static io.skoman.multitenant.constants.ITokenConstant.*;

public class UserUtil {

    public static UserDTO getCurrentUser() {
        Set<String> mappedAuthorities = new HashSet<>();
        final JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final Jwt jwt = (Jwt) auth.getPrincipal();

        String fullName = (String) getClaim(jwt.getClaims(), NAME_CLAIM, "");
        String email = (String) getClaim(jwt.getClaims(), OAuth2Constants.SCOPE_EMAIL, "");
        String tenant = (String) getClaim(jwt.getClaims(), TENANT_CLAIM, "");

        if (jwt.hasClaim(REALM_ACCESS_CLAIM)) {
            Map<String, Object> resourceAccess = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
            Collection<String> roles = (Collection<String>) resourceAccess.getOrDefault(ROLES_CLAIM, List.of());
            roles = roles.stream().filter(role -> !DEFAULT_KEYCLOAK_ROLES.contains(role)).toList();
            mappedAuthorities.addAll(roles);
        }

        return new UserDTO(
                getCurrentUserId(),
                tenant,
                fullName,
                email,
                mappedAuthorities
        );
    }

    public static Object getClaim(Map<String, Object> realms, String key, Object defaultValue) {
        return realms.getOrDefault(key, defaultValue);
    }

    public static String getCurrentUserId() {
        final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (String) jwt.getClaims().getOrDefault(USER_ID, null);
    }

    public static String getCurrentUserTenantId() {
        final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (String) jwt.getClaims().getOrDefault(TENANT_CLAIM, null);
    }

    public static String getKeycloakUserId(Response response) {
        return response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
    }
}
