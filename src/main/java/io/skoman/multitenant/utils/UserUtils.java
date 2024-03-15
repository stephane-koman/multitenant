package io.skoman.multitenant.utils;

import io.skoman.multitenant.dtos.UserDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;

import static io.skoman.multitenant.constants.ITokenConstant.*;
import static io.skoman.multitenant.constants.ITokenConstant.GROUPS;

public class UserUtils {

    public static UserDTO getCurrentUser() {
        Set<String> mappedAuthorities = new HashSet<>();
        final JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        final Jwt jwt = (Jwt) auth.getPrincipal();

        String fullName = (String) getClaim(jwt.getClaims(), "name", "");
        String email = (String) getClaim(jwt.getClaims(), "email", "");

        if (jwt.hasClaim(RESOURCE_ACCESS_CLAIM)) {
            Map<String, Object> resourceAccess = jwt.getClaimAsMap(RESOURCE_ACCESS_CLAIM);
            Map<String, Object> multitenantApp = (Map<String, Object>) resourceAccess.getOrDefault(MULTITENANT_APP_CLAIM, Map.of());
            var roles = (Collection<String>) multitenantApp.getOrDefault(ROLES_CLAIM, List.of());
            mappedAuthorities.addAll(roles);
        }

        return new UserDTO(
                getCurrentUserId(),
                null,
                fullName,
                email,
                mappedAuthorities
        );
    }

    private static Object getClaim(Map<String, Object> realms, String key, Object defaultValue) {
        return realms.getOrDefault(key, defaultValue);
    }

    public static String getCurrentUserId() {
        final Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (String) jwt.getClaims().getOrDefault("user_id", null);
    }
}
