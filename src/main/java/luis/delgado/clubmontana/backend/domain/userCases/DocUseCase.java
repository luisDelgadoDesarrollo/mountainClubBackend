package luis.delgado.clubmontana.backend.domain.userCases;

import luis.delgado.clubmontana.backend.domain.model.enums.PdfType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DocUseCase {

  void save(Long clubId, MultipartFile bylawsm, PdfType imageType);

  Resource get(Long clubId, PdfType imageType);
}
