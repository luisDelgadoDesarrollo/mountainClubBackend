package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "activity_image")
@Entity
public class ActivityImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long activityImageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "activity_id", nullable = false)
  private ActivityEntity activity;

  @Column private String image;

  @Column private String moment;
}
