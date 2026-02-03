package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.Us;
import luis.delgado.clubmontana.backend.domain.model.UsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UsUseCases {
  void create(Long clubId, Us us, Map<String, MultipartFile> files);

  void update(Long clubId, Us us, Map<String, MultipartFile> files);

  UsResponse get(Long clubId);
}
