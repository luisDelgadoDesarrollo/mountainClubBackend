package luis.delgado.clubmontana.backend.application.useCases;

import java.awt.*;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.enums.PdfType;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.userCases.DocUseCase;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class DocUseCaseImpl implements DocUseCase {
  private final FileStorageService fileStorageService;

  public DocUseCaseImpl(FileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @Override
  public void save(Long clubId, MultipartFile bylaws, PdfType pdfType) {
    fileStorageService.savePdf(clubId, bylaws, pdfType);
  }

  @NoAuthenticationNeeded
  @Override
  public Resource get(Long clubId, PdfType pdfType) {
    return fileStorageService.getPdf(clubId, pdfType);
  }
}
