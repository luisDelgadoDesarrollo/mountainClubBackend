package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "publicationLink")
@Entity
public class PublicationLinkEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long publicationLinkId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "publication_id", nullable = false, unique = true)
  private PublicationEntity publication;

  @Column private String link;

  @Column private String title;
}
