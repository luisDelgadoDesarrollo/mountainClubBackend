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
@Table(name = "article_variant")
@Entity
public class ArticleVariantEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long articleVariantId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id", nullable = false)
  private ArticleEntity article;

  @Column private String size;

  @Column private String color;

  @Column private Integer stock;

  @OneToMany(
      mappedBy = "articleVariant",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<ArticleVariantImageEntity> images = new HashSet<>();

  public void addImage(ArticleVariantImageEntity image) {
    images.add(image);
    image.setArticleVariant(this);
  }
}
