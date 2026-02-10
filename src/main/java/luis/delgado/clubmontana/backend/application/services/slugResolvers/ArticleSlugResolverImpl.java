package luis.delgado.clubmontana.backend.application.services.slugResolvers;

import luis.delgado.clubmontana.backend.api.exceptions.SlugNotFoundException;
import luis.delgado.clubmontana.backend.core.annotations.SlugResolver;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.ArticleSlugResolver;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ArticleEntityJpa;

@SlugResolver
public class ArticleSlugResolverImpl implements ArticleSlugResolver {
  private final ArticleEntityJpa articleEntityJpa;

  public ArticleSlugResolverImpl(ArticleEntityJpa articleEntityJpa) {
    this.articleEntityJpa = articleEntityJpa;
  }

  @Override
  public Long resolve(String articleSlug) {
    return articleEntityJpa
        .findBySlug(articleSlug)
        .orElseThrow(() -> new SlugNotFoundException(articleSlug, ArticleSlugResolver.class))
        .getArticleId();
  }
}
