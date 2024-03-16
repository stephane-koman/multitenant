package io.skoman.multitenant.security;

import io.skoman.multitenant.config.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

import static io.skoman.multitenant.constants.ITokenConstant.TENANT_CLAIM;
import static io.skoman.multitenant.utils.UserUtil.getClaim;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authToken = authHeader.substring(7);
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
        Jwt jwt = jwtDecoder.decode(authToken);

        if(isTokenValid(jwt)){
            String tenant = (String) getClaim(jwt.getClaims(), TENANT_CLAIM, "");
            TenantContext.setTenantInfo(tenant);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenValid(Jwt jwt) {
        Instant today = Instant.now();
        return Objects.requireNonNull(jwt.getExpiresAt()).getEpochSecond() > today.getEpochSecond() || jwt.getExpiresAt().getEpochSecond() == today.getEpochSecond();
    }

}
