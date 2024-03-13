package io.skoman.multitenant.services.impl;

import io.skoman.multitenant.daos.user.UserDAO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public Page<UserDTO> search(Pageable pageable) {
        return userDAO.users(pageable);
    }
}
