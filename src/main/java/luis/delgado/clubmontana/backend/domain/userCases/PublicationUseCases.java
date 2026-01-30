package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface PublicationUseCases {
  Publication create(Long clubId, Publication publication, Map<String, MultipartFile> files);

  void delete(Long clubId, Long publicationId);

  Publication update(
      Long clubId, Long publicationId, Publication publication, Map<String, MultipartFile> files);

  Pair<Publication, List<String>> getPublication(Long clubId, Long publicationId);
}
