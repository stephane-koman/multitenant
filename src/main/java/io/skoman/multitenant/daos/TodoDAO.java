package io.skoman.multitenant.daos;

import io.skoman.multitenant.dtos.TodoDTO;
import io.skoman.multitenant.entities.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodoDAO  extends JpaRepository<Todo, UUID> {
    @Query("select new io.skoman.multitenant.dtos.TodoDTO(t.id, t.name, t.tenant) from Todo t")
    Page<TodoDTO> todos(Pageable pageable);
}
