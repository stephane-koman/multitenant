package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.*;
import io.skoman.multitenant.services.KeycloakAdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/auth")
public class AuthController {

    private final KeycloakAdminApiService keycloakAdminApiService;

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<UserDTO> signup(@RequestBody SignupUserDTO userDTO){
        return new ResponseEntity<>(keycloakAdminApiService.signup(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginUserDTO userDTO){
        return ResponseEntity.ok(keycloakAdminApiService.login(userDTO));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO){
        return ResponseEntity.ok(keycloakAdminApiService.refreshToken(refreshTokenDTO));
    }

}
