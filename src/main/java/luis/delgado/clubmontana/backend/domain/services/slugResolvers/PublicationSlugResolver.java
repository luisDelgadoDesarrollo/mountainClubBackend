package luis.delgado.clubmontana.backend.domain.services.slugResolvers;

public interface PublicationSlugResolver {
  Long resolve(String articleSlug);
}
