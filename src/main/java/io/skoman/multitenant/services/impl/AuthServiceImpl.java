package io.skoman.multitenant.services.impl;

import io.skoman.multitenant.config.TenantContext;
import io.skoman.multitenant.daos.UserDAO;
import io.skoman.multitenant.dtos.LoginUserDTO;
import io.skoman.multitenant.dtos.RegisterUserDTO;
import io.skoman.multitenant.dtos.TenantCreaDTO;
import io.skoman.multitenant.dtos.TenantDTO;
import io.skoman.multitenant.entities.User;
import io.skoman.multitenant.exceptions.UserException;
import io.skoman.multitenant.services.AuthService;
import io.skoman.multitenant.services.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TenantService tenantService;

    @Override
    @Transactional
    public User signup(RegisterUserDTO dto) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<User> userFuture = executor.submit(() -> {
            Optional<User> userExists = userDAO.findByEmail(dto.email());

            if(userExists.isPresent())
                throw new UserException("Email address already used");

            TenantDTO tenantDTO = tenantService.addTenant(new TenantCreaDTO(dto.tenantName()));

            TenantContext.setTenantInfo(String.valueOf(tenantDTO.id()));

            User user = User.builder()
                    .fullName(dto.fullName())
                    .tenant(String.valueOf(tenantDTO.id()))
                    .email(dto.email())
                    .password(passwordEncoder.encode(dto.password()))
                    .build();

            return userDAO.save(user);
        });
        executor.close();
        return userFuture.get();
    }

    @Override
    public User authenticate(LoginUserDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        return userDAO.findByEmail(dto.email())
                .orElseThrow();
    }
}
