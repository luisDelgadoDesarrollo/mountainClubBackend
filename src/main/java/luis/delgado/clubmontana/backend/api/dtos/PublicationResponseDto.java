package luis.delgado.clubmontana.backend.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record PublicationResponseDto(
    Long publicationId,
    String slug,
    Long clubId,
    String title,
    String text,
    List<PublicationLinkDto> links,
    LocalDateTime createdAt,
    List<String> imagesPath) {}
