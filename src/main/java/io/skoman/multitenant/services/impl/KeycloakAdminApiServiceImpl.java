package io.skoman.multitenant.services.impl;

import io.skoman.multitenant.config.KeycloakAdminProperties;
import io.skoman.multitenant.dtos.UserCreaDTO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.dtos.UserSearchDTO;
import io.skoman.multitenant.exceptions.UserException;
import io.skoman.multitenant.mappers.UserMapper;
import io.skoman.multitenant.services.KeycloakAdminApiService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static io.skoman.multitenant.constants.ITokenConstant.DEFAULT_KEYCLOAK_ROLES;
import static io.skoman.multitenant.constants.ITokenConstant.TENANT_CLAIM;
import static io.skoman.multitenant.utils.UserUtil.getCurrentUserTenantId;
import static io.skoman.multitenant.utils.UserUtil.getKeycloakUserId;

@Service
@RequiredArgsConstructor
public class KeycloakAdminApiServiceImpl implements KeycloakAdminApiService {

    private final Keycloak keycloak;

    private final KeycloakAdminProperties kcaProperties;

    private RealmResource getRealm() {
        return keycloak.realm(kcaProperties.realm());
    }

    @Override
    public List<UserRepresentation> getUsers() {
        return getRealm().users().list();
    }

    @Override
    public List<UserRepresentation> searchUser(String search) {
        return getRealm().users().search(search);
    }

    @Override
    public Page<UserSearchDTO> searchUsers(String search, Pageable pageable) {
        List<UserRepresentation> userList = getRealm()
                .users().searchByAttributes(pageable.getPageNumber(),pageable.getPageSize(),null, true, search);

        List<UserSearchDTO> userDTOList = userList
                .stream().map(UserMapper.INSTANCE::userRepresentationToUserSearchDTO)
                .toList();

        int size = getRealm()
                .users().count(null, null, null, null,null, null,null, search);

        return new PageImpl<>(userDTOList, pageable, size);
    }

    @Override
    public UserRepresentation getUser(String userId) {
        return getRealm().users().get(userId).toRepresentation();
    }

    @Override
    public UserDTO createUser(UserCreaDTO userCreaDTO) {
        if(!userCreaDTO.password().equals(userCreaDTO.passwordConfirm()))
            throw new UserException("The two passwords don't matched");

        List<UserRepresentation> userRepresentationList = searchUser(userCreaDTO.username());
        verifyExistenceOfElement(userRepresentationList, "This username already in used");

        userRepresentationList = searchUser(userCreaDTO.email());
        verifyExistenceOfElement(userRepresentationList, "This email already in used");

        UserRepresentation userToCreate = UserMapper.INSTANCE.userCreateDTOToUserRepresentation(userCreaDTO);

        userToCreate.setEnabled(true);
        userToCreate.setEmailVerified(true);

        String tenantId = getCurrentUserTenantId();

        userToCreate.setAttributes(Map.of(TENANT_CLAIM, List.of(String.valueOf(tenantId))));

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setValue(userCreaDTO.password());
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setTemporary(false);

        userToCreate.setCredentials(List.of(passwordCred));

        Response response = getRealm().users().create(userToCreate);

        if(response.getStatus() != HttpStatus.CREATED.value())
            throw new RuntimeException("An error occurred during user creation");

        String userId = getKeycloakUserId(response);

        addRolesToUser(userId, userCreaDTO.roles());

        return new UserDTO(getKeycloakUserId(response), tenantId, userCreaDTO.firstName() + " " + userCreaDTO.lastName(), userCreaDTO.email(), userCreaDTO.roles());
    }

    private static void verifyExistenceOfElement(List<UserRepresentation> userRepresentationList, String message) {
        if(!userRepresentationList.isEmpty())
            throw new UserException(message);
    }

    @Override
    public Map<String, String> getUserGroups(String userId) {
        UserResource userResource = getRealm().users().get(userId);
        Map<String, String> existingGroups = new HashMap<>();
        userResource.groups().forEach(groupRepresentation -> existingGroups.put(groupRepresentation.getId(), groupRepresentation.getName()));
        return existingGroups;
    }

    @Override
    public void addGroupToUser(String userId, String groupId) {
        UserResource userResource = getRealm().users().get(userId);
        userResource.joinGroup(groupId);
    }

    @Override
    public void removeGroupFromUser(String userId, String groupId) {
        UserResource userResource = getRealm().users().get(userId);
        userResource.leaveGroup(groupId);
    }

    @Override
    public void addUserAttributeSiteToApp(String userId, String siteId, String appId) {
        UserResource userResource = getRealm().users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> map = userRepresentation.getAttributes();
        List<String> list = new ArrayList<>();
        if (map.containsKey(appId)) {
            list = map.get(appId);
            if (!list.contains(siteId)) {
                list.add(siteId);
            }
        } else {
            list.add(siteId);
        }
        map.put(appId, list);
        userRepresentation.setAttributes(map);
        userResource.update(userRepresentation);
    }

    @Override
    public void removeUserAttributeSiteFromApp(String userId, String siteId, String appId) {
        UserResource userResource = getRealm().users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> map = userRepresentation.getAttributes();
        List<String> list = new ArrayList<>();
        if (map.containsKey(appId)) {
            list = map.get(appId);
            list.remove(siteId);
        }
        map.put(appId, list);
        userRepresentation.setAttributes(map);
        userResource.update(userRepresentation);
    }

    @Override
    public List<GroupRepresentation> getGroups() {
        return getRealm().groups().groups();
    }

    @Override
    public List<RoleRepresentation> getRoles() {
        return getRealm()
                .roles()
                .list()
                .stream()
                .filter(role -> !DEFAULT_KEYCLOAK_ROLES.contains(role.getName()))
                .toList();
    }

    @Override
    public void addRolesToUser(String userId, List<String> roleNameList) {
        if(!roleNameList.isEmpty()){
            roleNameList.forEach(roleName -> addRoleToUser(userId, roleName));
        }
    }

    public void addRoleToUser(String userId, String roleName) {
        RoleResource roleResource = getRealm().roles().get(roleName);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(roleResource.toRepresentation());
        getRealm().users().get(userId).roles().realmLevel().add(roleToAdd);
    }

    @Override
    public void removeRoleFromUser(String userId, String roleName) {
        RoleResource roleResource = getRealm().roles().get(roleName);
        List<RoleRepresentation> roleToRemove = new LinkedList<>();
        roleToRemove.add(roleResource.toRepresentation());
        getRealm().users().get(userId).roles().realmLevel().remove(roleToRemove);
    }
}
