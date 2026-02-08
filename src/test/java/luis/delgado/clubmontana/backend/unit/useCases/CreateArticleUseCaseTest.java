package luis.delgado.clubmontana.backend.unit.useCases;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.application.useCases.ArticleUseCasesImpl;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.model.ArticleVariant;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ArticleRepository;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class CreateArticleUseCaseTest {

  @Mock private ArticleRepository articleRepository;

  @Mock private FileStorageService fileStorageService;

  @InjectMocks private ArticleUseCasesImpl articleUseCases;

  @Test
  void create_shouldSaveArticleAndStoreImages() {
    Long clubId = 1L;

    Article article =
        Article.builder()
            .title("Artículo test")
            .images(List.of(Image.builder().image("image-1").imageId(10L).parentId(1L).build()))
            .variants(
                List.of(
                    ArticleVariant.builder()
                        .articleVariantId(20L)
                        .images(
                            List.of(
                                Image.builder().image("image-2").imageId(30L).parentId(1L).build()))
                        .build()))
            .build();

    when(articleRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    Map<String, MultipartFile> files =
        Map.of(
            "image-1", mock(MultipartFile.class),
            "image-2", mock(MultipartFile.class),
            "image-ignored", mock(MultipartFile.class));

    Article result = articleUseCases.create(clubId, article, files);

    assertThat(result.getClubId()).isEqualTo(clubId);

    verify(articleRepository).save(article);

    verify(fileStorageService)
        .store(
            argThat(map -> map.size() == 1 && map.containsKey("image-1")),
            anyMap(),
            eq(article.getArticleId()),
            eq(clubId),
            eq(ImageType.ARTICLE));

    verify(fileStorageService)
        .store(
            argThat(map -> map.size() == 1 && map.containsKey("image-2")),
            anyMap(),
            eq(20L),
            eq(clubId),
            eq(ImageType.ARTICLE_VARIANT));
  }

  @Test
  void create_shouldNotCallStorageWhenNoImages() {
    Article article = Article.builder().images(List.of()).variants(List.of()).build();

    when(articleRepository.save(any())).thenReturn(article);

    articleUseCases.create(1L, article, Map.of());

    verify(fileStorageService, never()).store(any(), any(), any(), any(), any());
  }
}
