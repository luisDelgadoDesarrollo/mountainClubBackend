package luis.delgado.clubmontana.backend.unit.useCases;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.application.services.FileSystemFileStorageService;
import luis.delgado.clubmontana.backend.application.useCases.ActivityUseCasesImpl;
import luis.delgado.clubmontana.backend.domain.model.Activity;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class CreateActivityUseCaseTest {

  @Mock private ActivityRepository activityRepository;

  @Mock private FileSystemFileStorageService fileSystemImageStorageService;

  @InjectMocks private ActivityUseCasesImpl activityUserCase;

  @Test
  void create_happyPath() {
    // given
    Long clubId = 1L;
    Long activityId = 10L;

    Activity activity = new Activity();
    activity.setImages(
        List.of(
            Image.builder().image("img-1").imageId(14L).parentId(1L).build(),
            Image.builder().image("img-2").imageId(15L).parentId(1L).build()));
    activity.setStartDate(LocalDateTime.now());

    Activity saved = new Activity();
    saved.setActivityId(activityId);
    saved.setImages(activity.getImages());

    Map<String, MultipartFile> files =
        Map.of(
            "img-1", mock(MultipartFile.class),
            "img-2", mock(MultipartFile.class));

    when(activityRepository.saveActivity(any())).thenReturn(saved);

    // when
    Activity result = activityUserCase.createActivity(clubId, activity, files);

    // then
    assertThat(result).isSameAs(saved);

    verify(activityRepository).saveActivity(any());

    verify(fileSystemImageStorageService)
        .store(
            eq(files),
            eq(
                Map.of(
                    "img-1", 14L,
                    "img-2", 15L)),
            eq(activityId),
            eq(clubId),
            eq(ImageType.ACTIVITY));
  }

  @Test
  void create_whenFilesAndImagesCountMismatch_shouldThrowException() {
    // given
    Activity activity = new Activity();
    activity.setImages(List.of(Image.builder().image("img-1").imageId(1L).build()));
    activity.setStartDate(LocalDateTime.now());

    Map<String, MultipartFile> files =
        Map.of(
            "img-1", mock(MultipartFile.class),
            "img-2", mock(MultipartFile.class));

    Activity saved = new Activity();
    saved.setActivityId(1L);
    saved.setImages(activity.getImages());

    when(activityRepository.saveActivity(any())).thenReturn(saved);

    doThrow(new IllegalArgumentException())
        .when(fileSystemImageStorageService)
        .store(any(), any(), any(), any(), any());

    // when / then
    assertThatThrownBy(() -> activityUserCase.createActivity(1L, activity, files))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
