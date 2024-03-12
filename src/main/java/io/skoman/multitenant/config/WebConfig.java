package io.skoman.multitenant.config;

import io.skoman.multitenant.interceptors.RequestInterceptor;
import io.skoman.multitenant.services.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor(jwtService))
                .addPathPatterns("/**")
                .excludePathPatterns("/**/auth/**");
    }
}
