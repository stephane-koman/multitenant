package io.skoman.multitenant.controllers;

import io.skoman.multitenant.daos.user.UserDAO;
import io.skoman.multitenant.dtos.TodoCreaDTO;
import io.skoman.multitenant.dtos.TodoDTO;
import io.skoman.multitenant.services.TodoService;
import io.skoman.multitenant.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/v1/todos")
public class TodoController {

    private final TodoService todoService;
    private final JwtService jwtService;
    private final UserDAO userDAO;

    @PostMapping
    public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoCreaDTO dto){
        return new ResponseEntity<>(todoService.addTodo(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TodoDTO>> search(Pageable pageable){
        return ResponseEntity.ok(todoService.search(pageable));
    }
}
