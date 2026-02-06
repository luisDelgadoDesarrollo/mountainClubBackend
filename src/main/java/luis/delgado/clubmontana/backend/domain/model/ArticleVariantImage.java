package luis.delgado.clubmontana.backend.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArticleVariantImage {
  Long articleVariantImageId;
  Long articleVariantId;
  String image;
  String description;
}
