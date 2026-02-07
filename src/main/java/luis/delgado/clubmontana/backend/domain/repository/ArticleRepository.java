package luis.delgado.clubmontana.backend.domain.repository;

import luis.delgado.clubmontana.backend.domain.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepository {
  Article save(Article article);

  Article getArticle(Long clubId, Long articleId);

  void delete(Long clubId, Long articleId);

  Page<Article> getArticles(Long clubId, Pageable pageable);
}
