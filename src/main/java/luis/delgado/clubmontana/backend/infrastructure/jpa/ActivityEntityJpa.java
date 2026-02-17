package luis.delgado.clubmontana.backend.infrastructure.jpa;

import java.time.LocalDateTime;
import java.util.List;
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

  Optional<ActivityEntity> findBySlug(String slug);

  Boolean existsBySlug(String s);

  @Query(
"""
    select distinct a
    from ActivityEntity a
    left join fetch a.images
    where a.club.clubId = :clubId
      and a.endDate < :now
    order by a.endDate desc
""")
  List<ActivityEntity> findLastPastActivity(Long clubId, LocalDateTime now, Pageable pageable);

  @Query(
      """
          select distinct a
          from ActivityEntity a
          left join fetch a.images
          where a.club.clubId = :clubId
            and extract(year from a.startDate) = :year
          order by a.startDate desc
          """)
  List<ActivityEntity> findAllByYear(@Param("clubId") Long clubId, @Param("year") Integer year);

  @Query(
      """
          select distinct extract(year from a.startDate)
          from ActivityEntity a
          where a.club.clubId = :clubId
            and a.startDate is not null
          order by extract(year from a.startDate) desc
          """)
  List<Integer> getYears(@Param("clubId") Long clubId);
}
