package luis.delgado.clubmontana.backend.unit.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UpdatePublicationUseCaseTest {

  @Mock private PublicationRepository publicationRepository;

  @Mock private FileSystemFileStorageService fileSystemImageStorageService;

  @InjectMocks private PublicationUseCasesImpl publicationUseCases;

  @Test
  void update_happyPath_updatesPublicationAndStoresImages() {
    // given
    Long clubId = 1L;
    Long publicationId = 10L;

    Publication publication = new Publication();
    publication.setImages(
        List.of(PublicationImage.builder().publicationImageId(100L).image("image-1").build()));

    Publication saved = new Publication();
    saved.setPublicationId(publicationId);
    saved.setImages(publication.getImages());

    Map<String, MultipartFile> files =
        Map.of(
            "image-1",
            new MockMultipartFile("image-1", "photo.jpg", "image/jpeg", new byte[] {1, 2, 3}));

    when(publicationRepository.savePublication(any())).thenReturn(saved);

    // when
    Publication result = publicationUseCases.update(clubId, publicationId, publication, files);

    // then
    assertEquals(publicationId, result.getPublicationId());

    verify(publicationRepository).savePublication(any());

    verify(fileSystemImageStorageService)
        .deleteImages(clubId, ImageType.PUBLICATION, publicationId);

    verify(fileSystemImageStorageService)
        .store(
            eq(files),
            eq(Map.of("image-1", 100L)),
            eq(publicationId),
            eq(clubId),
            eq(ImageType.PUBLICATION));
  }
}
