package io.skoman.multitenant.dtos;

public record LoginUserDTO(String username, String email, String password, String firstName, String lastName, String tenantName) {
}
