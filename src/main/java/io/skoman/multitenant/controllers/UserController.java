package io.skoman.multitenant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.skoman.multitenant.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.skoman.multitenant.utils.UserUtils.getCurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserConnected(){
        return ResponseEntity.ok(getCurrentUser());
    }

}
