package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Article;

public interface ArticleRepository {
  Article save(Article article);
}
