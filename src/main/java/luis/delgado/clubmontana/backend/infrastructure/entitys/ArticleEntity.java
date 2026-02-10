package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "article")
public class ArticleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "article_id")
  private Long articleId;

  @Column(name = "slug", nullable = false, updatable = false, unique = true)
  private String slug;

  @Column(nullable = false)
  private String title;

  @Column private String description;

  @OneToMany(
      mappedBy = "article",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<ArticleImageEntity> images = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "club_id", nullable = false)
  private ClubEntity club;

  @OneToMany(
      mappedBy = "article",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<ArticleVariantEntity> variants = new HashSet<>();

  public void addImage(ArticleImageEntity image) {
    images.add(image);
    image.setArticle(this);
  }

  public void addVariant(ArticleVariantEntity variant) {
    variants.add(variant);
    variant.setArticle(this);
  }
}
