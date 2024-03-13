package io.skoman.multitenant.daos.user;

import io.skoman.multitenant.entities.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
