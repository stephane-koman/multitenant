package io.skoman.multitenant.mappers;

import io.skoman.multitenant.dtos.UserCreaDTO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.entities.user.Role;
import io.skoman.multitenant.entities.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User userCreaDTOToUser(UserCreaDTO dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserDTO userToUserDTO(User user);

    @Named("mapRoles")
    static List<String> mapRoles(Collection<Role> roles) {
        if(roles.isEmpty()) return Collections.emptyList();
        return roles.stream().map(Role::getName).toList();
    }
}
