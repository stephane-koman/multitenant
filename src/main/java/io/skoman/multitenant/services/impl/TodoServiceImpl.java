package io.skoman.multitenant.services.impl;

import io.skoman.multitenant.daos.TodoDAO;
import io.skoman.multitenant.dtos.TodoCreaDTO;
import io.skoman.multitenant.dtos.TodoDTO;
import io.skoman.multitenant.entities.Todo;
import io.skoman.multitenant.mappers.TodoMapper;
import io.skoman.multitenant.services.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoDAO todoDAO;

    @Override
    public TodoDTO addTodo(TodoCreaDTO dto) {
        Todo todo = TodoMapper.INSTANCE.todoCreaDTOToTodo(dto);
        Todo todoSaved = todoDAO.save(todo);
        return TodoMapper.INSTANCE.todoToTodoDTO(todoSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TodoDTO> search(Pageable pageable) {
        return todoDAO.todos(pageable);
    }
}