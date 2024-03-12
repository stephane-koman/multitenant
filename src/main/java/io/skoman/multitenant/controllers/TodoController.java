package io.skoman.multitenant.controllers;

import io.skoman.multitenant.dtos.TodoCreaDTO;
import io.skoman.multitenant.dtos.TodoDTO;
import io.skoman.multitenant.services.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoCreaDTO dto){
        return new ResponseEntity<>(todoService.addTodo(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<TodoDTO> search(Pageable pageable){
        return todoService.search(pageable);
    }
}
