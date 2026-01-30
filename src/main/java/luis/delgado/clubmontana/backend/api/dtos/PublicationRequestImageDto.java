package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record PublicationRequestImageDto(@NotBlank String image, String description) {}
