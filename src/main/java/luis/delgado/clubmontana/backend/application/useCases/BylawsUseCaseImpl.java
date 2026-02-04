package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.userCases.BylawsUseCase;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class BylawsUseCaseImpl implements BylawsUseCase {
  private final FileStorageService fileStorageService;

  public BylawsUseCaseImpl(FileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @Override
  public void save(Long clubId, MultipartFile bylaws) {
    fileStorageService.savePdf(clubId, bylaws, ImageType.BY_LAWS, "pylaws");
  }

  @NoAuthenticationNeeded
  @Override
  public Resource getBylaws(Long clubId) {
    return fileStorageService.getPdf(clubId, ImageType.BY_LAWS, "pylaws");
  }
}
