package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "usImage")
@Entity
public class UsImageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long usImageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_id", nullable = false)
  private UsEntity us;

  @Column private String image;

  @Column private String description;
}
