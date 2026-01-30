package luis.delgado.clubmontana.backend.infrastructure.mappers;

import luis.delgado.clubmontana.backend.domain.model.Publication;
import luis.delgado.clubmontana.backend.domain.model.PublicationImage;
import luis.delgado.clubmontana.backend.domain.model.PublicationLink;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ClubEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationImageEntity;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationLinkEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicationRepositoryMapper {

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
    publication
        .getImages()
        .forEach(
            publicationImage ->
                publicationEntity.addImage(
                    publicationImageToPublicationImageEntity(publicationImage)));
    publication
        .getLinks()
        .forEach(
            publicationLink ->
                publicationEntity.addLink(publicationLinkToPublicationLinkEntity(publicationLink)));
    return publicationEntity;
  }

  PublicationLinkEntity publicationLinkToPublicationLinkEntity(PublicationLink publicationLink);

  PublicationImageEntity publicationImageToPublicationImageEntity(
      PublicationImage publicationImage);

  @Mapping(target = "publicationId", source = "publication.publicationId")
  PublicationImage publicationImageEntityToPublicationImage(
      PublicationImageEntity publicationImageEntity);

  @Mapping(target = "publicationId", source = "publication.publicationId")
  PublicationLink publicationLinkEntityToPublicationLink(
      PublicationLinkEntity publicationLinkEntity);
}
