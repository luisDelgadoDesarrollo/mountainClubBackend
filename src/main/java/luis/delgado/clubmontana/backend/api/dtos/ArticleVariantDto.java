package luis.delgado.clubmontana.backend.api.dtos;

import java.util.List;

public record ArticleVariantDto(
    Long articleId,
    Long articleVariantId,
    String size,
    String color,
    Integer stock,
    List<String> images) {}
