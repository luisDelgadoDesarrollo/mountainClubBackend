package luis.delgado.clubmontana.backend.domain.model;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArticleVariant {
  Long articleVariantId;
  Long articleId;
  String size;
  String color;
  Integer stock;
  List<Image> images;
}
