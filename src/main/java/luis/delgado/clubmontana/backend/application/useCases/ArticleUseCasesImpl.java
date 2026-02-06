package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.model.ArticleImage;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariant;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariantImage;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ArticleRepository;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.userCases.ArticleUseCases;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class ArticleUseCasesImpl implements ArticleUseCases {

  private final ArticleRepository articleRepository;
  private final FileStorageService fileStorageService;

  public ArticleUseCasesImpl(
      ArticleRepository articleRepository, FileStorageService fileStorageService) {
    this.articleRepository = articleRepository;
    this.fileStorageService = fileStorageService;
  }

  @Override
  public Article create(Long clubId, Article article, Map<String, MultipartFile> files) {
    article.setClubId(clubId);
    Article articleSaved = articleRepository.save(article);
    saveArticleImages(articleSaved, files);
    saveArticleVariantsImages(articleSaved.getVariants(), files, clubId);
    return articleSaved;
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
              .collect(Collectors.toMap(ArticleImage::getImage, ArticleImage::getArticleImageId)),
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
                    .collect(
                        Collectors.toMap(
                            ArticleVariantImage::getImage,
                            ArticleVariantImage::getArticleVariantImageId)),
                articleVariant.getArticleVariantId(),
                clubId,
                ImageType.ARTICLE_VARIANT);
          }
        });
  }
}
