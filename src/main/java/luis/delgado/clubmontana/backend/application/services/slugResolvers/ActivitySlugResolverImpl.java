package luis.delgado.clubmontana.backend.application.services.slugResolvers;

import luis.delgado.clubmontana.backend.api.exceptions.SlugNotFoundException;
import luis.delgado.clubmontana.backend.core.annotations.SlugResolver;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.ActivitySlugResolver;
import luis.delgado.clubmontana.backend.infrastructure.jpa.ActivityEntityJpa;

@SlugResolver
public class ActivitySlugResolverImpl implements ActivitySlugResolver {
  private final ActivityEntityJpa activityEntityJpa;

  public ActivitySlugResolverImpl(ActivityEntityJpa activityEntityJpa) {
    this.activityEntityJpa = activityEntityJpa;
  }

  @Override
  public Long resolve(String slug) {
    return activityEntityJpa
        .findBySlug(slug)
        .orElseThrow(() -> new SlugNotFoundException(slug, ActivitySlugResolver.class))
        .getActivityId();
  }
}
