package io.skoman.multitenant.constants;

import java.util.List;

public interface IUserConstant {
    String FULL_NAME = "fullName";
    String ROLES = "roles";
    List<String> DEFAULT_KEYCLOAK_ROLES_FOR_SIGNUP = List.of("ADMIN", "USER");
}
