package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record LinkRequestDto(String title, @NotBlank String link) {}
