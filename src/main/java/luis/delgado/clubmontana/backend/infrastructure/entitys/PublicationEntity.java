package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "publication")
@Entity
public class PublicationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long publicationId;

  @Column private String title;

  @Column private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_id", nullable = false, unique = true)
  private ClubEntity club;
}
