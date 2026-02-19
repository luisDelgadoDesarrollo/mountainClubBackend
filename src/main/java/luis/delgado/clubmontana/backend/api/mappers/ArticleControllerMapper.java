package luis.delgado.clubmontana.backend.api.mappers;

import java.util.List;
import luis.delgado.clubmontana.backend.api.dtos.ArticleDto;
import luis.delgado.clubmontana.backend.api.dtos.ArticleVariantDto;
import luis.delgado.clubmontana.backend.api.dtos.CreateArticleDto;
import luis.delgado.clubmontana.backend.api.dtos.ResponseDto;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.util.Pair;

@Mapper(componentModel = "spring")
public interface ArticleControllerMapper {
  Article createArticleDtoToArticle(CreateArticleDto createArticleDto);

  @Mapping(target = "id", source = "articleId")
  ResponseDto articleToIdResponseDto(Article article);

  default ArticleDto articleToArticleDto(Pair<Article, List<String>> articleListPair) {
    if (articleListPair == null) return null;
    Article article = articleListPair.getFirst();
    List<String> articleImages =
        articleListPair.getSecond().stream()
            .filter(image -> image.matches(".*" + ImageType.ARTICLE + "_\\d+.*"))
            .toList();

    List<String> articleVariantImages =
        articleListPair.getSecond().stream()
            .filter(image -> image.contains(String.valueOf(ImageType.ARTICLE_VARIANT)))
            .toList();
    List<ArticleVariantDto> articleVariantDtos =
        article.getVariants().stream()
            .map(
                variant ->
                    new ArticleVariantDto(
                        article.getArticleId(),
                        variant.getArticleVariantId(),
                        variant.getSize(),
                        variant.getColor(),
                        variant.getStock(),
                        articleVariantImages.stream()
                            .filter(
                                variantImage ->
                                    variantImage.contains(
                                        ImageType.ARTICLE_VARIANT
                                            + "_"
                                            + variant.getArticleVariantId()))
                            .toList()))
            .toList();

    return new ArticleDto(
        article.getClubId(),
        article.getSlug(),
        article.getArticleId(),
        article.getTitle(),
        article.getDescription(),
        articleImages,
        articleVariantDtos);
  }

  default List<ArticleDto> articleListToArticleDtoList(
      List<Pair<Article, List<String>>> pairArticles) {
    return pairArticles.stream().map(this::articleToArticleDto).toList();
  }
}
