package luis.delgado.clubmontana.backend.application.useCases;

import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.userCases.BylawsUseCase;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class BylawsUseCaseImpl implements BylawsUseCase {
  @Override
  public void save(Long clubId, MultipartFile bylaws) {}

  @Override
  public Resource getBylaws(Long clubId) {
    return null;
  }
}
