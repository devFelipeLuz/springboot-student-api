package br.com.backend.dto.request;

import jakarta.validation.constraints.NotNull;

public record SchoolYearRequest(
        @NotNull Integer year
) {}
