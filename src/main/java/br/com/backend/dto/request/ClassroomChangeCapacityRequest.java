package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record ClassroomChangeCapacityRequest(
        @NotNull
        int newCapacity
) {
}
