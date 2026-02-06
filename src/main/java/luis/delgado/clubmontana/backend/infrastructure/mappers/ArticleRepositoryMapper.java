package luis.delgado.clubmontana.backend.infrastructure.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.model.ArticleImage;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariant;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariantImage;
import luis.delgado.clubmontana.backend.infrastructure.entitys.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleRepositoryMapper {
  default Set<ArticleVariantEntity> articleVariantsListToArticleVariantEntitySet(
      List<ArticleVariant> variants) {
    if (variants == null) return null;
    return variants.stream()
        .map(this::articleVariantsToArticleVariantEntity)
        .collect(Collectors.toSet());
  }

  default ArticleVariantEntity articleVariantsToArticleVariantEntity(
      ArticleVariant articleVariant) {
    if (articleVariant == null) return null;

    ArticleVariantEntity entity = new ArticleVariantEntity();
    entity.setArticleVariantId(articleVariant.getArticleVariantId());
    entity.setSize(articleVariant.getSize());
    entity.setColor(articleVariant.getColor());
    entity.setStock(articleVariant.getStock());

    if (articleVariant.getImages() != null) {
      articleVariant
          .getImages()
          .forEach(img -> entity.addImage(articleVariantImageToArticleVariantImageEntity(img)));
    }

    return entity;
  }

  ArticleVariantImageEntity articleVariantImageToArticleVariantImageEntity(
      ArticleVariantImage articleVariantImage);

  @Mapping(target = "articleVariantId", source = "articleVariant.articleVariantId")
  ArticleVariantImage articleVariantImageEntityToArticleVariantImage(
      ArticleVariantImageEntity articleVariantsImage);

  default ArticleEntity articleToArticleEntity(Article article) {
    if (article == null) return null;

    ArticleEntity articleEntity = new ArticleEntity();

    articleEntity.setClub(ClubEntity.builder().clubId(article.getClubId()).build());
    articleEntity.setArticleId(article.getArticleId());
    articleEntity.setTitle(article.getTitle());
    articleEntity.setDescription(article.getDescription());
    if (article.getImages() != null) {
      article
          .getImages()
          .forEach(
              articleImage ->
                  articleEntity.addImage(articleImageToArticleImageEntity(articleImage)));
    }
    if (article.getVariants() != null) {
      article
          .getVariants()
          .forEach(
              variant -> articleEntity.addVariant(articleVariantsToArticleVariantEntity(variant)));
    }
    return articleEntity;
  }

  ArticleImageEntity articleImageToArticleImageEntity(ArticleImage articleImage);

  @Mapping(target = "clubId", source = "club.clubId")
  Article articleEntityToArticle(ArticleEntity articleEntity);

  @Mapping(target = "articleId", source = "article.articleId")
  ArticleVariant articleVariantEntityToArticleVariants(ArticleVariantEntity articleVariantEntity);
}
