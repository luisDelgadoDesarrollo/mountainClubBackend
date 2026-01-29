package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreatePublicationRequestDto(
        @NotBlank String title, String text, List<PublicationImageDto> images, List<PublicationLinkDto> links) {}
