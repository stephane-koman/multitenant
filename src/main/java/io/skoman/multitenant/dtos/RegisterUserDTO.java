package io.skoman.multitenant.dtos;

public record RegisterUserDTO(String email, String password, String firstName, String lastName, String tenantName) {}
