package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "activity")
public class ActivityEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "activity_id")
  private Long activityId;

  @Column(name = "slug", nullable = false, updatable = false, unique = true)
  private String slug;

  @Column(nullable = false)
  private String title;

  @Column private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_id", nullable = false)
  private ClubEntity club;

  @Column(name = "created_at", nullable = true, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "affiliate_price")
  private BigDecimal affiliatePrice;

  @Column(name = "no_affiliate_price")
  private BigDecimal noAffiliatePrice;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @OneToMany(
      mappedBy = "activity",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<ActivityImageEntity> images = new HashSet<>();

  public void addImage(ActivityImageEntity image) {
    images.add(image);
    image.setActivity(this);
  }
}
