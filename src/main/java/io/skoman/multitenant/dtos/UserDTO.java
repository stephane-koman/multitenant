package io.skoman.multitenant.dtos;

import java.util.UUID;

public record UserDTO(UUID id, String tenant, String fullName, String email) {
}
