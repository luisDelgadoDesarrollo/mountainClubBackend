package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record PublicationLinkDto(
    Long linkId, Long publicationId, String title, @NotBlank String link) {}
