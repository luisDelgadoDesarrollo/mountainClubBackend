package luis.delgado.clubmontana.backend.domain.model;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Article {
  Long clubId;
  Long articleId;
  String title;
  String description;
  List<Image> images;
  List<ArticleVariant> variants;
}
