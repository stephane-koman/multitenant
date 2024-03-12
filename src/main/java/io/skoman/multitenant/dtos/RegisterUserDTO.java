package io.skoman.multitenant.dtos;

public record RegisterUserDTO(String email, String password, String fullName, String tenantName) {
}
