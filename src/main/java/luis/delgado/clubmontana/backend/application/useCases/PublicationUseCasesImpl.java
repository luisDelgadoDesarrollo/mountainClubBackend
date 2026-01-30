package luis.delgado.clubmontana.backend.application.useCases;

import java.util.Map;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.application.services.FileSystemImageStorageService;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.PublicationImage;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class PublicationUseCasesImpl implements PublicationUseCases {

  private final PublicationRepository publicationRepository;
  private final FileSystemImageStorageService fileSystemImageStorageService;

  public PublicationUseCasesImpl(
      PublicationRepository publicationRepository,
      FileSystemImageStorageService fileSystemImageStorageService) {
    this.publicationRepository = publicationRepository;
    this.fileSystemImageStorageService = fileSystemImageStorageService;
  }

  @Override
  public Publication create(
      Long clubId, Publication publication, Map<String, MultipartFile> files) {
    publication.setClubId(clubId);
    Publication publicationSaved = publicationRepository.createPublication(publication);

    fileSystemImageStorageService.store(
        files,
        publicationSaved.getImages().stream()
            .collect(
                Collectors.toMap(
                    PublicationImage::getImage, PublicationImage::getPublicationImageId)),
        publicationSaved.getPublicationId(),
        clubId,
        ImageType.PUBLICATION);
    return publicationSaved;
  }
}
