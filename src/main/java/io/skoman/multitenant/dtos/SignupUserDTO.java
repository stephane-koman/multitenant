package io.skoman.multitenant.dtos;

public record SignupUserDTO(String username, String email, String password, String passwordConfirm, String firstName, String lastName, String tenantName) {}
