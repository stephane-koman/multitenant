package io.skoman.multitenant.constants;

import java.util.List;

public interface ITokenConstant {
    String REALM_ACCESS_CLAIM = "realm_access";
    String ROLES_CLAIM = "roles";
    String NAME_CLAIM = "name";
    String EMAIL_CLAIM = "email";
    String TENANT_CLAIM = "tenant_id";
    List<String> DEFAULT_KEYCLOAK_ROLES = List.of("offline_access", "default-roles-multitenant-realm", "uma_authorization");
    String LOGOUT_URL_SUFFIX = "/protocol/openid-connect/logout";
    String LOGIN_URL_SUFFIX = "/protocol/openid-connect/token";

}
