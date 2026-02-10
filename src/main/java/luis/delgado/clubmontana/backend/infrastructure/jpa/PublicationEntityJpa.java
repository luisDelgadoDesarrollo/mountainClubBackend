package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.PublicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublicationEntityJpa extends JpaRepository<PublicationEntity, Long> {
  void deleteByClub_ClubIdAndPublicationId(Long clubId, Long publicationId);

  @Query(
"""
  select distinct p
  from PublicationEntity p
  left join fetch p.images
  left join fetch p.links
  where p.club.clubId = :clubId
    and p.publicationId = :publicationId
""")
  Optional<PublicationEntity> findByClubAndId(
      @Param("clubId") Long clubId, @Param("publicationId") Long publicationId);

  @Query(
      """
            select distinct p
            from PublicationEntity p
            left join fetch p.images
            left join fetch p.links
            where p.club.clubId = :clubId
          """)
  Page<PublicationEntity> findByClub_ClubId(Long clubId, Pageable pageable);

  Optional<PublicationEntity> findBySlug(String slug);
}
