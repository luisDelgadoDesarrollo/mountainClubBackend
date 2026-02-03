package luis.delgado.clubmontana.backend.domain.userCases;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BylawsUseCase {

  void save(Long clubId, MultipartFile bylaws);

  Resource getBylaws(Long clubId);
}
