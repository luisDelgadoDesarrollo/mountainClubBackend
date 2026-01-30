package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record PublicationRequestLinkDto(String title, @NotBlank String link) {}
