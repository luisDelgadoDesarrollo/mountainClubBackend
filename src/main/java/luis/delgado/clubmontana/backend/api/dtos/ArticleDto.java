package luis.delgado.clubmontana.backend.api.dtos;

import java.util.List;

public record ArticleDto(
    Long clubId,
    String slug,
    Long articleId,
    String title,
    String description,
    List<String> imagePath,
    List<ArticleVariantDto> variants) {}
