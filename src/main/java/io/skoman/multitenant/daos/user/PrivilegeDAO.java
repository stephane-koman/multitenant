package io.skoman.multitenant.daos.user;

import io.skoman.multitenant.entities.user.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDAO extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);
}
