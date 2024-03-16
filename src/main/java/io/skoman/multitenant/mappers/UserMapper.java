package io.skoman.multitenant.mappers;

import io.skoman.multitenant.dtos.UserCreaDTO;
import io.skoman.multitenant.dtos.UserDTO;
import io.skoman.multitenant.dtos.UserSearchDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "fullName", expression = "java(userRepresentation.getFirstName() + \" \" + userRepresentation.getLastName() )")
    UserDTO userRepresentationToUserDTO(UserRepresentation userRepresentation);

    UserSearchDTO userRepresentationToUserSearchDTO(UserRepresentation userRepresentation);

    UserRepresentation userCreateDTOToUserRepresentation(UserCreaDTO userCreaDTO);

}
