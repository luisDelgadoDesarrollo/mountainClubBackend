package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArticleImage {
  Long articleImageId;
  Long articleId;
  String image;
  String description;
}
