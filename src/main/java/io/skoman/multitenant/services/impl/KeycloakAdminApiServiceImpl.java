package io.skoman.multitenant.services.impl;

import io.skoman.multitenant.config.KeycloakAdminProperties;
import io.skoman.multitenant.services.KeycloakAdminApiService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KeycloakAdminApiServiceImpl implements KeycloakAdminApiService {

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private KeycloakAdminProperties kcaProperties;

    @Override
    public List<UserRepresentation> getUsers() {
        return keycloak.realm(kcaProperties.realm()).users().list();
    }

    @Override
    public List<UserRepresentation> searchUser(String search) {
        return keycloak.realm(kcaProperties.realm()).users().search(search);
    }

    @Override
    public UserRepresentation getUser(String userId) {
        return keycloak.realm(kcaProperties.realm()).users().get(userId).toRepresentation();
    }

    @Override
    public Map<String, String> getUserGroups(String userId) {
        UserResource userResource = keycloak.realm(kcaProperties.realm()).users().get(userId);
        Map<String, String> existingGroups = new HashMap<>();
        userResource.groups().forEach(groupRepresentation -> existingGroups.put(groupRepresentation.getId(), groupRepresentation.getName()));
        return existingGroups;
    }

    @Override
    public void addGroupToUser(String userId, String groupId) {
        UserResource userResource = keycloak.realm(kcaProperties.realm()).users().get(userId);
        userResource.joinGroup(groupId);
    }

    @Override
    public void removeGroupFromUser(String userId, String groupId) {
        UserResource userResource = keycloak.realm(kcaProperties.realm()).users().get(userId);
        userResource.leaveGroup(groupId);
    }

    @Override
    public void addUserAttributeSiteToApp(String userId, String siteId, String appId) {
        UserResource userResource = keycloak.realm(kcaProperties.realm()).users().get(userId);
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
        UserResource userResource = keycloak.realm(kcaProperties.realm()).users().get(userId);
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
        return keycloak.realm(kcaProperties.realm()).groups().groups();
    }

    @Override
    public List<RoleRepresentation> getRoles() {
        return keycloak.realm(kcaProperties.realm()).roles().list();
    }

    @Override
    public void addRoleToUser(String userId, String roleName) {
        RoleResource roleResource = keycloak.realm(kcaProperties.realm()).roles().get(roleName);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(roleResource.toRepresentation());
        keycloak.realm(kcaProperties.realm()).users().get(userId).roles().realmLevel().add(roleToAdd);
    }

    @Override
    public void removeRoleFromUser(String userId, String roleName) {
        RoleResource roleResource = keycloak.realm(kcaProperties.realm()).roles().get(roleName);
        List<RoleRepresentation> roleToRemove = new LinkedList<>();
        roleToRemove.add(roleResource.toRepresentation());
        keycloak.realm(kcaProperties.realm()).users().get(userId).roles().realmLevel().remove(roleToRemove);
    }
}
