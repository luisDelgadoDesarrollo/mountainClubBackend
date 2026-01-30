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
  @JoinColumn(name = "club_id", nullable = false)
  private Long clubId;

  @Column private String image;

  @Column private String description;
}
