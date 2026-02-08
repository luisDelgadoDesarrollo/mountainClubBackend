package luis.delgado.clubmontana.backend.unit.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class UpdateActivityUseCaseTest {

  @Mock private ActivityRepository activityRepository;

  @Mock private FileSystemFileStorageService fileSystemImageStorageService;

  @InjectMocks private ActivityUseCasesImpl activityUseCases;

  @Test
  void update_happyPath_updatesPublicationAndStoresImages() {
    // given
    Long clubId = 1L;
    Long activityId = 10L;

    Activity activity = new Activity();
    activity.setImages(
        List.of(Image.builder().imageId(100L).parentId(1L).image("image-1").build()));

    Activity saved = new Activity();
    saved.setActivityId(activityId);
    saved.setImages(activity.getImages());

    Map<String, MultipartFile> files =
        Map.of(
            "image-1",
            new MockMultipartFile("image-1", "photo.jpg", "image/jpeg", new byte[] {1, 2, 3}));

    when(activityRepository.saveActivity(any())).thenReturn(saved);

    // when
    Activity result = activityUseCases.updateActivity(clubId, activityId, activity, files);

    // then
    assertEquals(activityId, result.getActivityId());

    verify(activityRepository).saveActivity(any());

    verify(fileSystemImageStorageService).deleteImages(clubId, ImageType.ACTIVITY, activityId);

    verify(fileSystemImageStorageService)
        .store(
            eq(files),
            eq(Map.of("image-1", 100L)),
            eq(activityId),
            eq(clubId),
            eq(ImageType.ACTIVITY));
  }
}
