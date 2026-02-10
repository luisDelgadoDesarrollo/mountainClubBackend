package luis.delgado.clubmontana.backend.application.services.slugResolvers;

import luis.delgado.clubmontana.backend.api.exceptions.SlugNotFoundException;
import luis.delgado.clubmontana.backend.core.annotations.SlugResolver;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.ClubSlugResolver;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ClubEntityJpa;

@SlugResolver
public class ClubSlugResolverImpl implements ClubSlugResolver {

  private final ClubEntityJpa clubEntityJpa;

  public ClubSlugResolverImpl(ClubEntityJpa clubEntityJpa) {
    this.clubEntityJpa = clubEntityJpa;
  }

  @Override
  public Long resolve(String clubSlug) {
    return clubEntityJpa
        .findBySlug(clubSlug)
        .orElseThrow(() -> new SlugNotFoundException(clubSlug, ClubSlugResolver.class))
        .getClubId();
  }
}
