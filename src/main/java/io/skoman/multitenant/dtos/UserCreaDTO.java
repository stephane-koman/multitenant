package io.skoman.multitenant.dtos;

import java.util.List;

public record UserCreaDTO(String username, String email, String firstName, String lastName, String password, String passwordConfirm, List<String> roles) {}
