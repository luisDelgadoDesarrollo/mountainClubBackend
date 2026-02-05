package luis.delgado.clubmontana.backend.domain.services;

import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.model.enums.PdfType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  Resource loadImage(String url);

  void store(
      Map<String, MultipartFile> files,
      Map<String, Long> imageIds,
      Long ownerId,
      Long clubId,
      ImageType imageType);

  void deleteImages(Long clubId, ImageType imageType, Long ownerId);

  List<String> getImages(Long clubId, Long ownerId, ImageType imageType);

  void savePdf(Long clubId, MultipartFile file, PdfType imageType);

  Resource getPdf(Long clubId, PdfType imageType);
}
