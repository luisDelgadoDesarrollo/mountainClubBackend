package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Us;
import luis.delgado.clubmontana.backend.domain.model.UsImage;
import luis.delgado.clubmontana.backend.domain.model.UsResponse;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.UsRepository;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.userCases.UsUseCases;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class UsUseCasesImpl implements UsUseCases {

  private final FileStorageService fileStorageService;
  private final UsRepository usRepository;

  public UsUseCasesImpl(FileStorageService fileStorageService, UsRepository usRepository) {
    this.fileStorageService = fileStorageService;
    this.usRepository = usRepository;
  }

  @Override
  public void create(Long clubId, Us us, Map<String, MultipartFile> files) {
    Us usSaved = usRepository.save(us, clubId);
    AtomicLong counter = new AtomicLong(1L);
    fileStorageService.store(
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
    fileStorageService.deleteImages(clubId, ImageType.US, 1L);
    AtomicLong counter = new AtomicLong(1L);
    fileStorageService.store(
        files,
        usSaved.getImages().stream()
            .collect(Collectors.toMap(UsImage::getImage, _ -> counter.getAndIncrement())),
        1L,
        clubId,
        ImageType.US);
  }

  @NoAuthenticationNeeded
  @Override
  public UsResponse get(Long clubId) {
    Us us = usRepository.get(clubId);
    List<String> images = fileStorageService.getImages(clubId, 1L, ImageType.US);
    return new UsResponse(clubId, us.getText(), images);
  }
}
