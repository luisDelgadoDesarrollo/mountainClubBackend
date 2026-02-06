package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "article_variant_image")
@Entity
public class ArticleVariantImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long articleVariantImageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_variant_id", nullable = false)
  private ArticleVariantEntity articleVariant;

  @Column private String image;

  @Column private String description;
}
