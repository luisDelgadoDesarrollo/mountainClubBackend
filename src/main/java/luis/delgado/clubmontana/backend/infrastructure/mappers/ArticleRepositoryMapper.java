package luis.delgado.clubmontana.backend.infrastructure.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariant;
import luis.delgado.clubmontana.backend.domain.model.Image;
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

  @Mapping(target = "articleVariantImageId", source = "imageId")
  @Mapping(target = "articleVariant.articleVariantId", source = "parentId")
  ArticleVariantImageEntity articleVariantImageToArticleVariantImageEntity(
      Image articleVariantImage);

  @Mapping(target = "imageId", source = "articleVariantImageId")
  @Mapping(target = "parentId", source = "articleVariant.articleVariantId")
  Image articleVariantImageEntityToArticleVariantImage(
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

  @Mapping(target = "articleImageId", source = "imageId")
  @Mapping(target = "article.articleId", source = "parentId")
  ArticleImageEntity articleImageToArticleImageEntity(Image articleImage);

  @Mapping(target = "clubId", source = "club.clubId")
  Article articleEntityToArticle(ArticleEntity articleEntity);

  @Mapping(target = "articleId", source = "article.articleId")
  ArticleVariant articleVariantEntityToArticleVariants(ArticleVariantEntity articleVariantEntity);

  @Mapping(target = "imageId", source = "articleImageId")
  @Mapping(target = "parentId", source = "article.articleId")
  Image articleImageEntityToImage(ArticleImageEntity articleImageEntity);
}
