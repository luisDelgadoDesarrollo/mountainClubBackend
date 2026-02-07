package luis.delgado.clubmontana.backend.infrastructure.repositorys;

import jakarta.transaction.Transactional;
import luis.delgado.clubmontana.backend.domain.model.Article;
import luis.delgado.clubmontana.backend.domain.repository.ArticleRepository;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ArticleEntityJpa;
import luis.delgado.clubmontana.backend.infrastructure.mappers.ArticleRepositoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class ArticleRepositoryImpl implements ArticleRepository {

  private final ArticleEntityJpa articleEntityJpa;
  private final ArticleRepositoryMapper articleRepositoryMapper;

  public ArticleRepositoryImpl(
      ArticleEntityJpa articleEntityJpa, ArticleRepositoryMapper articleRepositoryMapper) {
    this.articleEntityJpa = articleEntityJpa;
    this.articleRepositoryMapper = articleRepositoryMapper;
  }

  @Override
  public Article save(Article article) {
    return articleRepositoryMapper.articleEntityToArticle(
        articleEntityJpa.save(articleRepositoryMapper.articleToArticleEntity(article)));
  }

  @Override
  public Article getArticle(Long clubId, Long articleId) {
    return articleRepositoryMapper.articleEntityToArticle(
        articleEntityJpa.findByClubAndId(clubId, articleId).orElseThrow());
  }

  @Override
  public void delete(Long clubId, Long articleId) {
    articleEntityJpa.deleteByClub_ClubIdAndArticleId(clubId, articleId);
  }

  @Override
  public Page<Article> getArticles(Long clubId, Pageable pageable) {
    return articleEntityJpa
        .findByClub_ClubId(clubId, pageable)
        .map(articleRepositoryMapper::articleEntityToArticle);
  }
}
