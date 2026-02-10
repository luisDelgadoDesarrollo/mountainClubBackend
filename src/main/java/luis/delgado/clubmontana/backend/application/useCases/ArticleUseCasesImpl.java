package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariant;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ArticleRepository;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.services.SlugFactory;
import luis.delgado.clubmontana.backend.domain.userCases.ArticleUseCases;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class ArticleUseCasesImpl implements ArticleUseCases {

  private final ArticleRepository articleRepository;
  private final FileStorageService fileStorageService;
  private final SlugFactory slugFactory;

  public ArticleUseCasesImpl(
      ArticleRepository articleRepository,
      FileStorageService fileStorageService,
      SlugFactory slugFactory) {
    this.articleRepository = articleRepository;
    this.fileStorageService = fileStorageService;
    this.slugFactory = slugFactory;
  }

  @Override
  public Article create(Long clubId, Article article, Map<String, MultipartFile> files) {
    article.setClubId(clubId);
    article.setSlug(slugFactory.makeSlug(article.getTitle()));
    Article articleSaved = articleRepository.save(article);
    saveArticleImages(articleSaved, files);
    saveArticleVariantsImages(articleSaved.getVariants(), files, clubId);
    return articleSaved;
  }

  @Override
  public Article update(
      Long clubId, Long articleId, Article article, Map<String, MultipartFile> files) {
    Article articleOld = articleRepository.getArticle(clubId, articleId);
    article.setClubId(clubId);
    article.setArticleId(articleId);
    Article articleSaved = articleRepository.save(article);
    fileStorageService.deleteImages(clubId, ImageType.ARTICLE, articleId);
    saveArticleImages(articleSaved, files);
    articleOld
        .getVariants()
        .forEach(
            articleVariant ->
                fileStorageService.deleteImages(
                    clubId, ImageType.ARTICLE_VARIANT, articleVariant.getArticleVariantId()));
    saveArticleVariantsImages(articleSaved.getVariants(), files, clubId);
    return articleSaved;
  }

  @Override
  public void delete(Long clubId, Long articleId) {
    Article articleOld = articleRepository.getArticle(clubId, articleId);
    articleRepository.delete(clubId, articleId);
    fileStorageService.deleteImages(clubId, ImageType.ARTICLE, articleId);
    articleOld
        .getVariants()
        .forEach(
            articleVariant ->
                fileStorageService.deleteImages(
                    clubId, ImageType.ARTICLE_VARIANT, articleVariant.getArticleVariantId()));
  }

  @NoAuthenticationNeeded
  @Override
  public Pair<Article, List<String>> get(Long clubId, Long articleId) {
    Article article = articleRepository.getArticle(clubId, articleId);
    return buildPair(article);
  }

  @NoAuthenticationNeeded
  @Override
  public List<Pair<Article, List<String>>> getAll(Long clubId, Pageable pageable) {
    return articleRepository.getArticles(clubId, pageable).map(this::buildPair).toList();
  }

  private void saveArticleImages(Article article, Map<String, MultipartFile> files) {
    Map<String, MultipartFile> filesFiltered =
        files.entrySet().stream()
            .filter(
                file ->
                    article.getImages().stream()
                        .anyMatch(articleImage -> articleImage.getImage().equals(file.getKey())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (!filesFiltered.isEmpty()) {
      fileStorageService.store(
          filesFiltered,
          article.getImages().stream()
              .collect(Collectors.toMap(Image::getImage, Image::getParentId)),
          article.getArticleId(),
          article.getClubId(),
          ImageType.ARTICLE);
    }
  }

  private void saveArticleVariantsImages(
      List<ArticleVariant> articleVariants, Map<String, MultipartFile> files, Long clubId) {
    articleVariants.forEach(
        articleVariant -> {
          Map<String, MultipartFile> filesFiltered =
              files.entrySet().stream()
                  .filter(
                      file ->
                          articleVariant.getImages().stream()
                              .anyMatch(
                                  articleVariantImage ->
                                      articleVariantImage.getImage().equals(file.getKey())))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
          if (!filesFiltered.isEmpty()) {
            fileStorageService.store(
                filesFiltered,
                articleVariant.getImages().stream()
                    .collect(Collectors.toMap(Image::getImage, Image::getParentId)),
                articleVariant.getArticleVariantId(),
                clubId,
                ImageType.ARTICLE_VARIANT);
          }
        });
  }

  private Pair<Article, List<String>> buildPair(Article article) {
    List<String> images =
        fileStorageService.getImages(
            article.getClubId(), article.getArticleId(), ImageType.ARTICLE);
    article
        .getVariants()
        .forEach(
            variant -> {
              if (!variant.getImages().isEmpty()) {
                images.addAll(
                    fileStorageService.getImages(
                        article.getClubId(),
                        variant.getArticleVariantId(),
                        ImageType.ARTICLE_VARIANT));
              }
            });

    return Pair.of(article, images);
  }
}
