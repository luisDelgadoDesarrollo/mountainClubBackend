package luis.delgado.clubmontana.backend.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "article_image")
public class ArticleImageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long articleImageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_id", nullable = false)
  private ArticleEntity article;

  @Column private String image;

  @Column private String description;
}
