package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.*;
import io.skoman.multitenant.entities.User;
import io.skoman.multitenant.mappers.UserMapper;
import io.skoman.multitenant.security.JwtService;
import io.skoman.multitenant.services.AuthService;
import io.skoman.multitenant.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static io.skoman.multitenant.constants.ITenantConstant.TENANT_ID;
import static io.skoman.multitenant.constants.ITenantConstant.TENANT_NAME;
import static io.skoman.multitenant.constants.IUserConstant.FULL_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authenticationService;
    private final TenantService tenantService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterUserDTO registerUserDto) throws InterruptedException, ExecutionException {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserDTO(registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        TenantDTO tenant = tenantService.findByTenantId(UUID.fromString(authenticatedUser.getTenant()));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(TENANT_ID, tenant.id());
        extraClaims.put(TENANT_NAME, tenant.name());
        extraClaims.put(FULL_NAME, authenticatedUser.getFullName());
        String jwtToken = jwtService.generateToken(extraClaims, authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
