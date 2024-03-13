package io.skoman.multitenant.dtos;

import java.util.Collection;
import java.util.UUID;

public record UserDTO(UUID id, String tenant, String firstName, String lastName, String email, Collection<String> roles) {
}
