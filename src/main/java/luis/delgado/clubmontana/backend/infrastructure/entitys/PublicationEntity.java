package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "publication")
public class PublicationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "publication_id")
  private Long publicationId;

  @Column(nullable = false)
  private String title;

  @Column private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_id", nullable = false)
  private ClubEntity club;

  @OneToMany(
      mappedBy = "publication",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<PublicationImageEntity> images = new HashSet<>();

  @Column(name = "created_at", nullable = true, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(
      mappedBy = "publication",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<PublicationLinkEntity> links = new HashSet<>();

  public void addImage(PublicationImageEntity image) {
    images.add(image);
    image.setPublication(this);
  }

  public void addLink(PublicationLinkEntity link) {
    links.add(link);
    link.setPublication(this);
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
