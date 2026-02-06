package luis.delgado.clubmontana.backend.domain.userCases;

import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.Article;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleUseCases {
  Article create(Long clubId, Article article, Map<String, MultipartFile> files);
}
