package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.LoginResponse;
import io.skoman.multitenant.dtos.LoginUserDTO;
import io.skoman.multitenant.dtos.RegisterUserDTO;
import io.skoman.multitenant.entities.User;
import io.skoman.multitenant.services.AuthService;
import io.skoman.multitenant.services.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static io.skoman.multitenant.constants.ITenantConstant.TENANT_ID;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    private final AuthService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDto) throws InterruptedException, ExecutionException {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(TENANT_ID, authenticatedUser.getTenant());
        String jwtToken = jwtService.generateToken(extraClaims, authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
