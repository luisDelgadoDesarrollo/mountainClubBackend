package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record PublicationImageDto(@NotBlank String image, String desc) {}
