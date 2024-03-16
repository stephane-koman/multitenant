package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.skoman.multitenant.constants.ITokenConstant.TENANT_CLAIM;
import static io.skoman.multitenant.utils.UserUtil.getCurrentUser;
import static io.skoman.multitenant.utils.UserUtil.getCurrentUserTenantId;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/users")
public class UserController {

    @Value("${config.keycloak.realm}")
    private String realm;

    @Autowired
    Keycloak keycloak;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserConnected(){
        return ResponseEntity.ok(getCurrentUser());
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<UserRepresentation> search(Pageable pageable) throws Exception {
        return keycloak.realm(realm).users()
                .searchByAttributes(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        true,
                        true,
                        TENANT_CLAIM + ":" + getCurrentUserTenantId()
                );
    }
}
