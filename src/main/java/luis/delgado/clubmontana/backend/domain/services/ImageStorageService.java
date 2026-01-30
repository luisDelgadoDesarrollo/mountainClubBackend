package luis.delgado.clubmontana.backend.domain.services;

import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
  void store(
      Map<String, MultipartFile> files,
      Map<String, Long> imageIds,
      Long publicationId,
      Long clubId,
      ImageType imageType);

  void deleteImages(Long clubId, ImageType imageType, Long publicationId);

  List<String> getImages(Long clubId, Long publicationId, ImageType imageType);
}
