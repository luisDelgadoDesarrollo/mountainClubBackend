package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.springframework.web.multipart.MultipartFile;

public interface PublicationUseCases {
  Publication create(Long clubId, Publication publication, Map<String, MultipartFile> files);

  void delete(Long clubId, Long publicationId);
}
