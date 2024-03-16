package io.skoman.multitenant.services;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;

public interface KeycloakAdminApiService {
    List<UserRepresentation> getUsers();
    List<UserRepresentation> searchUser(String search);
    UserRepresentation getUser(String userId);
    Map<String, String> getUserGroups(String userId);
    void addGroupToUser(String userId, String groupId);
    void removeGroupFromUser(String userId, String groupId);
    void addUserAttributeSiteToApp(String userId, String siteId, String appId);
    void removeUserAttributeSiteFromApp(String userId, String siteId, String appId);
    List<GroupRepresentation> getGroups();
    List<RoleRepresentation> getRoles();
    void addRoleToUser(String userId, String roleName);
    void removeRoleFromUser(String userId, String roleName);
}
