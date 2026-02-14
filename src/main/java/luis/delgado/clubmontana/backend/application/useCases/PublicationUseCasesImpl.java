package luis.delgado.clubmontana.backend.application.useCases;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import luis.delgado.clubmontana.backend.core.annotations.NoAuthenticationNeeded;
import luis.delgado.clubmontana.backend.core.annotations.UseCase;
import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.enums.ImageType;
import luis.delgado.clubmontana.backend.domain.repository.PublicationRepository;
import luis.delgado.clubmontana.backend.domain.services.FileStorageService;
import luis.delgado.clubmontana.backend.domain.services.SlugFactory;
import luis.delgado.clubmontana.backend.domain.userCases.PublicationUseCases;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

@UseCase
public class PublicationUseCasesImpl implements PublicationUseCases {

  private final PublicationRepository publicationRepository;
  private final FileStorageService fileStorageService;
  private final SlugFactory slugFactory;

  public PublicationUseCasesImpl(
      PublicationRepository publicationRepository,
      FileStorageService fileStorageService,
      SlugFactory slugFactory) {
    this.publicationRepository = publicationRepository;
    this.fileStorageService = fileStorageService;
    this.slugFactory = slugFactory;
  }

  @Override
  public Publication create(
      Long clubId, Publication publication, Map<String, MultipartFile> files) {
    publication.setClubId(clubId);
    publication.setSlug(
        slugFactory.makeSlug(publication.getTitle(), publicationRepository::existsBySlug));
    Publication publicationSaved = publicationRepository.savePublication(publication);

    fileStorageService.store(
        files,
        publicationSaved.getImages().stream()
            .collect(Collectors.toMap(Image::getImage, Image::getImageId)),
        publicationSaved.getPublicationId(),
        clubId,
        ImageType.PUBLICATION);
    return publicationSaved;
  }

  @Override
  public void delete(Long clubId, Long publicationId) {
    publicationRepository.deletePublication(clubId, publicationId);
    fileStorageService.deleteImages(clubId, ImageType.PUBLICATION, publicationId);
  }

  @Override
  public Publication update(
      Long clubId, Long publicationId, Publication publication, Map<String, MultipartFile> files) {
    publication.setClubId(clubId);
    publication.setPublicationId(publicationId);
    Publication publicationSaved = publicationRepository.savePublication(publication);
    fileStorageService.deleteImages(clubId, ImageType.PUBLICATION, publicationId);
    fileStorageService.store(
        files,
        publicationSaved.getImages().stream()
            .collect(Collectors.toMap(Image::getImage, Image::getImageId)),
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
        fileStorageService.getImages(
            publication.getClubId(), publication.getPublicationId(), ImageType.PUBLICATION);
    return Pair.of(publication, images);
  }

  @NoAuthenticationNeeded
  @Override
  public Page<Pair<Publication, List<String>>> getPublications(Long clubId, Pageable pageable) {
    Page<Publication> publications = publicationRepository.getPublications(clubId, pageable);
    return publications.map(
        publication -> {
          List<String> images =
              fileStorageService.getImages(
                  publication.getClubId(), publication.getPublicationId(), ImageType.PUBLICATION);

          return Pair.of(publication, images);
        });
  }

  @NoAuthenticationNeeded
  @Override
  public Optional<Pair<Publication, List<String>>> getLastPublication(Long clubId) {
    Publication publication = publicationRepository.getLastPublication(clubId);
    if (publication == null) return Optional.empty();
    List<String> images =
        fileStorageService.getImages(
            publication.getClubId(), publication.getPublicationId(), ImageType.PUBLICATION);
    return Optional.of(Pair.of(publication, images));
  }
}
