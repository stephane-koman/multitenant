package io.skoman.multitenant.services;

import io.skoman.multitenant.dtos.UserCreaDTO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.dtos.UserSearchDTO;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface KeycloakAdminApiService {
    List<UserRepresentation> getUsers();
    List<UserRepresentation> searchUser(String search);
    Page<UserSearchDTO> searchUsers(String search, Pageable pageable);
    UserRepresentation getUser(String userId);
    UserDTO createUser(UserCreaDTO userCreaDTO);
    Map<String, String> getUserGroups(String userId);
    void addGroupToUser(String userId, String groupId);
    void removeGroupFromUser(String userId, String groupId);
    void addUserAttributeSiteToApp(String userId, String siteId, String appId);
    void removeUserAttributeSiteFromApp(String userId, String siteId, String appId);
    List<GroupRepresentation> getGroups();
    List<RoleRepresentation> getRoles();
    void addRolesToUser(String userId, List<String> roleNameList);
    void removeRoleFromUser(String userId, String roleName);
}
