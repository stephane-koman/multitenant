package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.LoginUserDTO;
import io.skoman.multitenant.dtos.TenantCreaDTO;
import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.services.TenantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpStatus;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.skoman.multitenant.constants.ITokenConstant.TENANT_CLAIM;
import static io.skoman.multitenant.utils.UserUtil.addDefaultRolesToUser;
import static io.skoman.multitenant.utils.UserUtil.getKeycloakUserId;

@RestController
@RequestMapping("${apiPrefix}/v1/auth")
public class AuthController {

    @Value("${config.keycloak.realm}")
    private String realm;
    @Autowired
    Keycloak keycloak;

    @Autowired
    TenantService tenantService;

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) throws Exception {
        request.logout();
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> signup(@RequestBody LoginUserDTO userDTO){

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();
        UserRepresentation userRepresentation = getUserRepresentation(userDTO);

        Response response = userResource.create(userRepresentation);
        String userId = getKeycloakUserId(response);

        addDefaultRolesToUser(realmResource, userId);

        if(response.getStatus() != HttpStatus.CREATED.value())
            throw new RuntimeException("");

        return new ResponseEntity<>("User created with success", HttpStatus.CREATED);
    }

    @Transactional
    private UserRepresentation getUserRepresentation(LoginUserDTO userDTO) {
        TenantDTO tenant = tenantService.addTenant(new TenantCreaDTO(userDTO.tenantName()));
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        userRepresentation.setAttributes(Map.of(TENANT_CLAIM, List.of(String.valueOf(tenant.id()))));

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setValue(userDTO.password());
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setTemporary(false);

        userRepresentation.setCredentials(List.of(passwordCred));

        return userRepresentation;
    }
}
