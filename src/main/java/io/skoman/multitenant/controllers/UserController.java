package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.mappers.UserMapper;
import io.skoman.multitenant.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.skoman.multitenant.utils.UserUtils.getCurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> authenticatedUser(){
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(getCurrentUser());
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> searchUsers(Pageable pageable){
        return ResponseEntity.ok(userService.search(pageable));
    }
}
