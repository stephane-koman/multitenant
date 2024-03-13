package io.skoman.multitenant.services;

import io.skoman.multitenant.dtos.LoginUserDTO;
import io.skoman.multitenant.dtos.RegisterUserDTO;
import io.skoman.multitenant.entities.user.User;

import java.util.concurrent.ExecutionException;

public interface AuthService {
    User signup(RegisterUserDTO dto) throws InterruptedException, ExecutionException;
    User authenticate(LoginUserDTO dto);
}
