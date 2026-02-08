package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.Image;
import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.PublicationLink;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationImageEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationLinkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicationRepositoryMapper {

  @Mapping(target = "clubId", source = "club.clubId")
  Publication publicationEntityToPublication(PublicationEntity publicationEntity);

  default PublicationEntity publicationToPublicationEntity(Publication publication) {
    if (publication == null) return null;
    PublicationEntity publicationEntity = new PublicationEntity();
    publicationEntity.setPublicationId(publication.getPublicationId());
    publicationEntity.setTitle(publication.getTitle());
    publicationEntity.setText(publication.getText());
    ClubEntity club = new ClubEntity();
    club.setClubId(publication.getClubId());
    publicationEntity.setClub(club);
    if (publication.getImages() != null) {
      publication
          .getImages()
          .forEach(
              publicationImage ->
                  publicationEntity.addImage(
                      publicationImageToPublicationImageEntity(publicationImage)));
    }
    if (publication.getLinks() != null) {
      publication
          .getLinks()
          .forEach(
              publicationLink ->
                  publicationEntity.addLink(
                      publicationLinkToPublicationLinkEntity(publicationLink)));
    }
    return publicationEntity;
  }

  PublicationLinkEntity publicationLinkToPublicationLinkEntity(PublicationLink publicationLink);

  @Mapping(target = "publicationImageId", source = "imageId")
  @Mapping(target = "publication.publicationId", source = "parentId")
  PublicationImageEntity publicationImageToPublicationImageEntity(Image publicationImage);

  @Mapping(target = "imageId", source = "publicationImageId")
  @Mapping(target = "parentId", source = "publication.publicationId")
  Image publicationImageEntityToPublicationImage(PublicationImageEntity publicationImageEntity);

  @Mapping(target = "publicationId", source = "publication.publicationId")
  PublicationLink publicationLinkEntityToPublicationLink(
      PublicationLinkEntity publicationLinkEntity);
}
