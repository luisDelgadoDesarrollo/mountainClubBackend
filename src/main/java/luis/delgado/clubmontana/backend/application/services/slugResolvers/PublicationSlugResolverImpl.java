package luis.delgado.clubmontana.backend.application.services.slugResolvers;

import luis.delgado.clubmontana.backend.api.exceptions.SlugNotFoundException;
import luis.delgado.clubmontana.backend.core.annotations.SlugResolver;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.PublicationSlugResolver;
import luis.delgado.clubmontana.backend.infrastructure.jpa.PublicationEntityJpa;

@SlugResolver
public class PublicationSlugResolverImpl implements PublicationSlugResolver {

  private final PublicationEntityJpa publicationEntityJpa;

  public PublicationSlugResolverImpl(PublicationEntityJpa publicationEntityJpa) {
    this.publicationEntityJpa = publicationEntityJpa;
  }

  @Override
  public Long resolve(String articleSlug) {
    return publicationEntityJpa
        .findBySlug(articleSlug)
        .orElseThrow(() -> new SlugNotFoundException(articleSlug, PublicationSlugResolver.class))
        .getPublicationId();
  }
}
