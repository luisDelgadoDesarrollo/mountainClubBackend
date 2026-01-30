package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.application.services.FileSystemImageStorageService;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.PublicationImage;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.domain.services.ImageStorageService;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class PublicationUseCasesImpl implements PublicationUseCases {

  private final PublicationRepository publicationRepository;
  private final ImageStorageService imageStorageService;

  public PublicationUseCasesImpl(
      PublicationRepository publicationRepository,
      FileSystemImageStorageService imageStorageService) {
    this.publicationRepository = publicationRepository;
    this.imageStorageService = imageStorageService;
  }

  @Override
  public Publication create(
      Long clubId, Publication publication, Map<String, MultipartFile> files) {
    publication.setClubId(clubId);
    Publication publicationSaved = publicationRepository.savePublication(publication);

    imageStorageService.store(
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

  @Override
  public void delete(Long clubId, Long publicationId) {
    publicationRepository.deletePublication(clubId, publicationId);
    imageStorageService.deleteImages(clubId, ImageType.PUBLICATION, publicationId);
  }

  @Override
  public Publication update(
      Long clubId, Long publicationId, Publication publication, Map<String, MultipartFile> files) {
    publication.setClubId(clubId);
    publication.setPublicationId(publicationId);
    Publication publicationSaved = publicationRepository.savePublication(publication);
    imageStorageService.deleteImages(clubId, ImageType.PUBLICATION, publicationId);
    imageStorageService.store(
        files,
        publicationSaved.getImages().stream()
            .collect(
                Collectors.toMap(
                    PublicationImage::getImage, PublicationImage::getPublicationImageId)),
        publicationId,
        clubId,
        ImageType.PUBLICATION);

    return publicationSaved;
  }

  @NoAuthenticationNeeded
  @Override
  public Pair<Publication, List<String>> getPublication(Long clubId, Long publicationId) {
    Publication publication = publicationRepository.getPublication(clubId, publicationId);
    List<String> images =
        imageStorageService.getImages(
            publication.getClubId(), publication.getPublicationId(), ImageType.PUBLICATION);
    return new Pair<>(publication, images);
  }
}
