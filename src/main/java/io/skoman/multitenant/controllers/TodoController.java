package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.TodoCreaDTO;
import io.skoman.multitenant.dtos.TodoDTO;
import io.skoman.multitenant.services.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoCreaDTO dto, Principal principal){
        return new ResponseEntity<>(todoService.addTodo(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TodoDTO>> search(Pageable pageable){
        return ResponseEntity.ok(todoService.search(pageable));
    }
}
