package luis.delgado.clubmontana.backend.domain.services.slugResolvers;

public interface ArticleSlugResolver {
  Long resolve(String articleSlug);
}
