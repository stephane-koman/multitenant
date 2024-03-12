package io.skoman.multitenant.dtos;

public record LoginResponse(String token, long expiresIn) {
}
