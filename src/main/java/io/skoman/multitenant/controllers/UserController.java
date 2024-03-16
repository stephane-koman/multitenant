package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.dtos.UserCreaDTO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.dtos.UserSearchDTO;
import io.skoman.multitenant.services.KeycloakAdminApiService;
import io.skoman.multitenant.services.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static io.skoman.multitenant.constants.ITokenConstant.TENANT_CLAIM;
import static io.skoman.multitenant.utils.UserUtil.getCurrentUser;
import static io.skoman.multitenant.utils.UserUtil.getCurrentUserTenantId;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/users")
public class UserController {

    @Value("${config.keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    private final TenantService tenantService;
    private final KeycloakAdminApiService keycloakAdminApiService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserConnected(){
        UserDTO userDTO = getCurrentUser();
        TenantDTO tenantDTO = tenantService.findByTenantId(UUID.fromString(userDTO.tenant()));
        return ResponseEntity.ok(new UserDTO(userDTO.id(), tenantDTO.name(), userDTO.fullName(), userDTO.email(), userDTO.roles()));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreaDTO userCreaDTO) throws Exception {
        return new ResponseEntity<>(keycloakAdminApiService.createUser(userCreaDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<UserSearchDTO> search(Pageable pageable) throws Exception {
        return keycloakAdminApiService.searchUsers(TENANT_CLAIM + ":" + getCurrentUserTenantId(), pageable);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) throws Exception {
        request.logout();
    }
}
