package luis.delgado.clubmontana.backend.unit.useCases;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import luis.delgado.clubmontana.backend.application.useCases.PublicationUseCasesImpl;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.PublicationImage;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class CreatePublicationUseCaseTest {

  @Mock private PublicationRepository publicationRepository;

  @Mock private FileSystemFileStorageService fileSystemImageStorageService;

  @InjectMocks private PublicationUseCasesImpl publicationUseCases;

  @Test
  void create_happyPath() {
    // given
    Long clubId = 1L;
    Long publicationId = 10L;

    Publication publication = new Publication();
    publication.setImages(
        List.of(
            PublicationImage.builder().image("img-1").publicationImageId(12L).build(),
            PublicationImage.builder().image("img-2").publicationImageId(13L).build()));

    Publication saved = new Publication();
    saved.setPublicationId(publicationId);
    saved.setImages(publication.getImages());

    Map<String, MultipartFile> files =
        Map.of(
            "img-1", mock(MultipartFile.class),
            "img-2", mock(MultipartFile.class));

    when(publicationRepository.savePublication(any())).thenReturn(saved);

    // when
    Publication result = publicationUseCases.create(clubId, publication, files);

    // then
    assertThat(result).isSameAs(saved);

    verify(publicationRepository).savePublication(any());

    verify(fileSystemImageStorageService)
        .store(
            eq(files),
            eq(
                Map.of(
                    "img-1", 12L,
                    "img-2", 13L)),
            eq(publicationId),
            eq(clubId),
            eq(ImageType.PUBLICATION));
  }

  @Test
  void create_whenFilesAndImagesCountMismatch_shouldThrowException() {
    // given
    Publication publication = new Publication();
    publication.setImages(
        List.of(PublicationImage.builder().image("img-1").publicationImageId(1L).build()));

    Map<String, MultipartFile> files =
        Map.of(
            "img-1", mock(MultipartFile.class),
            "img-2", mock(MultipartFile.class));

    Publication saved = new Publication();
    saved.setPublicationId(1L);
    saved.setImages(publication.getImages());

    when(publicationRepository.savePublication(any())).thenReturn(saved);

    doThrow(new IllegalArgumentException())
        .when(fileSystemImageStorageService)
        .store(any(), any(), any(), any(), any());

    // when / then
    assertThatThrownBy(() -> publicationUseCases.create(1L, publication, files))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
