package io.skoman.multitenant.interceptors;

import io.skoman.multitenant.config.TenantContext;
import io.skoman.multitenant.services.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static io.skoman.multitenant.constants.ISwaggerConstant.SWAGGER_API_DOCS;
import static io.skoman.multitenant.constants.ISwaggerConstant.SWAGGER_UI;
import static io.skoman.multitenant.constants.ITokenConstant.HEADER_TOKEN_NAME;
import static io.skoman.multitenant.constants.ITokenConstant.HEADER_TOKEN_PREFIX;

@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws IOException {
        String requestURI = request.getRequestURI();
        String authHeader = request.getHeader(HEADER_TOKEN_NAME);

        if(request.getServletPath().contains(SWAGGER_UI) || request.getServletPath().contains(SWAGGER_API_DOCS))
            return true;

        if (authHeader == null || !authHeader.startsWith(HEADER_TOKEN_PREFIX)) {
            response.getWriter().write("JWT Token IS WRONG");
            response.setStatus(405);
            return false;
        }
        final String jwt = authHeader.substring(7);
        String tenantID = jwtService.extractTenant(jwt);

        System.out.println("RequestURI " + requestURI + " Search for TenantID  :: " + tenantID);
        if (tenantID == null) {
            response.getWriter().write("TenantID not present in the JWT Token");
            response.setStatus(400);
            return false;
        }

        TenantContext.setTenantInfo(tenantID);
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView
    ) throws Exception {
        TenantContext.clear();
    }
}
