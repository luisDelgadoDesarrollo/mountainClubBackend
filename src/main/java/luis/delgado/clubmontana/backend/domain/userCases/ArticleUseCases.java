package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.List;
import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleUseCases {
  Article create(Long clubId, Article article, Map<String, MultipartFile> files);

  Article update(Long clubId, Long articleId, Article article, Map<String, MultipartFile> files);

  void delete(Long clubId, Long articleId);

  Pair<Article, List<String>> get(Long clubId, Long articleId);

  List<Pair<Article, List<String>>> getAll(Long clubId, Pageable pageable);
}
