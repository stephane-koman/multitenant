package io.skoman.multitenant.dtos;

import java.util.Collection;
import java.util.UUID;

public record UserDTO(String id, String tenant, String fullName, String email, Collection<String> roles) {
}
