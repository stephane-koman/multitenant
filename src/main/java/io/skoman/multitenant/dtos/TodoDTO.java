package io.skoman.multitenant.dtos;

import java.util.UUID;

public record TodoDTO(UUID id, String name, String tenant) {
}
