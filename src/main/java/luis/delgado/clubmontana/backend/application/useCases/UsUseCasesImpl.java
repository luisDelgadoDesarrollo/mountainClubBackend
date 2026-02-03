package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Us;
import luis.delgado.clubmontana.backend.domain.model.UsImage;
import luis.delgado.clubmontana.backend.domain.model.UsResponse;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.UsRepository;
import luis.delgado.clubmontana.backend.domain.services.ImageStorageService;
import luis.delgado.clubmontana.backend.domain.userCases.UsUseCases;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class UsUseCasesImpl implements UsUseCases {

  private final ImageStorageService imageStorageService;
  private final UsRepository usRepository;

  public UsUseCasesImpl(ImageStorageService imageStorageService, UsRepository usRepository) {
    this.imageStorageService = imageStorageService;
    this.usRepository = usRepository;
  }

  @Override
  public void create(Long clubId, Us us, Map<String, MultipartFile> files) {
    Us usSaved = usRepository.save(us, clubId);
    AtomicLong counter = new AtomicLong(1L);
    imageStorageService.store(
        files,
        usSaved.getImages().stream()
            .collect(Collectors.toMap(UsImage::getImage, _ -> counter.getAndIncrement())),
        1L,
        clubId,
        ImageType.US);
  }

  @Override
  public void update(Long clubId, Us us, Map<String, MultipartFile> files) {
    us.setClubId(clubId);
    Us usSaved = usRepository.update(us, clubId);
    imageStorageService.deleteImages(clubId, ImageType.US, 1L);
    AtomicLong counter = new AtomicLong(1L);
    imageStorageService.store(
        files,
        usSaved.getImages().stream()
            .collect(Collectors.toMap(UsImage::getImage, _ -> counter.getAndIncrement())),
        1L,
        clubId,
        ImageType.US);
  }

  @Override
  public UsResponse get(Long clubId) {
    Us us = usRepository.get(clubId);
    List<String> images = imageStorageService.getImages(clubId, 1L, ImageType.US);
    return new UsResponse(clubId, us.getText(), images);
  }
}
