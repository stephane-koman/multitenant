package io.skoman.multitenant.mappers;

import io.skoman.multitenant.dtos.TodoCreaDTO;
import io.skoman.multitenant.dtos.TodoDTO;
import io.skoman.multitenant.entities.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TodoMapper {
    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    Todo todoCreaDTOToTodo(TodoCreaDTO dto);

    TodoDTO todoToTodoDTO(Todo todo);
}
