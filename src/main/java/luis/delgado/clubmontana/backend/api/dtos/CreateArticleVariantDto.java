package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateArticleVariantDto(
    @NotBlank String size, String color, Integer stock, List<ImageRequestDto> images) {}
