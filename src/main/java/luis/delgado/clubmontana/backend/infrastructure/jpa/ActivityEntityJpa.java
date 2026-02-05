package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.util.Optional;
import luis.delgado.clubmontana.backend.infrastructure.entitys.ActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityEntityJpa extends JpaRepository<ActivityEntity, Long> {
  void deleteByClub_ClubIdAndActivityId(Long clubId, Long activityId);

  @Query(
      """
            select distinct a
            from ActivityEntity a
            left join fetch a.images
            where a.club.clubId = :clubId
              and a.activityId = :activityId
          """)
  Optional<ActivityEntity> findByClubAndId(
      @Param("clubId") Long clubId, @Param("activityId") Long activityId);

  @Query(
      """
                select distinct a
                from ActivityEntity a
                left join fetch a.images
                where a.club.clubId = :clubId
              """)
  Page<ActivityEntity> findByClub_ClubId(Long clubId, Pageable pageable);
}
