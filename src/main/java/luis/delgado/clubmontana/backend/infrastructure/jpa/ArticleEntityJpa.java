package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleEntityJpa extends JpaRepository<ArticleEntity, Long> {

  @Query(
      """
              select distinct a
              from ArticleEntity a
              left join fetch a.images
              left join fetch a.variants
              where a.club.clubId = :clubId
                and a.articleId = :articleId
            """)
  Optional<ArticleEntity> findByClubAndId(
      @Param("clubId") Long clubId, @Param("articleId") Long articleId);

  void deleteByClub_ClubIdAndArticleId(Long clubId, Long articleId);

  @Query(
      """
                select distinct a
                from ArticleEntity a
                left join fetch a.images
                left join fetch a.variants
                where a.club.clubId = :clubId
              """)
  Page<ArticleEntity> findByClub_ClubId(Long clubId, Pageable pageable);

  Optional<ArticleEntity> findBySlug(String articleSlug);

  Boolean existsBySlug(String s);
}
