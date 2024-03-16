package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.dtos.UserSearchDTO;
import io.skoman.multitenant.services.KeycloakAdminApiService;
import io.skoman.multitenant.services.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) throws Exception {
        request.logout();
    }

    /*@GetMapping
    public List<UserRepresentation> search(Pageable pageable) throws Exception {
        return keycloak.realm(realm).users()
                .searchByAttributes(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        true,
                        true,
                        TENANT_CLAIM + ":" + getCurrentUserTenantId()
                );
    }*/

    @GetMapping
    public Page<UserSearchDTO> search(Pageable pageable) throws Exception {
        return keycloakAdminApiService.searchUsers(TENANT_CLAIM + ":" + getCurrentUserTenantId(), pageable);
    }
}
