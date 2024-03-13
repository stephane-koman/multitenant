package io.skoman.multitenant.services;

import io.skoman.multitenant.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDTO> search(Pageable pageable);
}
