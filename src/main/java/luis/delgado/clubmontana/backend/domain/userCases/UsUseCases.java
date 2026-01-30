package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.UsRequest;
import luis.delgado.clubmontana.backend.domain.model.UsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UsUseCases {
  UsResponse create(Long clubId, UsRequest usRequest, Map<String, MultipartFile> files);

  UsResponse update(Long clubId, UsRequest usRequest, Map<String, MultipartFile> files);

  UsResponse get(Long clubId);
}
