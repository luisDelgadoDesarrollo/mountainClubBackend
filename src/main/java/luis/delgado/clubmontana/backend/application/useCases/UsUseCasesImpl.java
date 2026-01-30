package luis.delgado.clubmontana.backend.application.useCases;

import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.UsRequest;
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
  public UsResponse create(Long clubId, UsRequest usRequest, Map<String, MultipartFile> files) {

    imageStorageService.store(files, null, 1L, clubId, ImageType.US);
    return null;
  }

  @Override
  public UsResponse update(Long clubId, UsRequest usRequest, Map<String, MultipartFile> files) {
    return null;
  }

  @Override
  public UsResponse get(Long clubId) {
    return null;
  }
}
