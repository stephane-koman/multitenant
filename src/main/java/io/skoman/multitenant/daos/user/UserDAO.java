package io.skoman.multitenant.daos.user;

import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDAO extends JpaRepository<User, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE email = :email")
    Optional<User> findByEmail(String email);

    @Query("select new io.skoman.multitenant.dtos.UserDTO(u.id, u.tenant, u.firstName, u.lastName, u.email) from User u")
    Page<UserDTO> users(Pageable pageable);
}
