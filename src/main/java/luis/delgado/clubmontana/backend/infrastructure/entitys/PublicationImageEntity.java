package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "publicationImage")
@Entity
public class PublicationImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long publicationImageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "publication_id", nullable = false, unique = true)
  private PublicationEntity publication;

  @Column private String imageName;

  @Column private String description;
}
