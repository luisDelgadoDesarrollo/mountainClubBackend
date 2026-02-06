package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateArticleDto(
    @NotBlank String title,
    String description,
    List<ImageRequestDto> images,
    List<CreateArticleVariantDto> variants) {}
