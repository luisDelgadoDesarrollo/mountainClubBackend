package luis.delgado.clubmontana.backend.config;

import java.util.List;
import luis.delgado.clubmontana.backend.infrastructure.resolvers.ActivityIDArgumentResolver;
import luis.delgado.clubmontana.backend.infrastructure.resolvers.ArticleIdArgumentResolver;
import luis.delgado.clubmontana.backend.infrastructure.resolvers.ClubIdArgumentResolver;
import luis.delgado.clubmontana.backend.infrastructure.resolvers.PublicationIdArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final ClubIdArgumentResolver clubIdArgumentResolver;
  private final ArticleIdArgumentResolver articleIdArgumentResolver;
  private final PublicationIdArgumentResolver publicationIdArgumentResolver;
  private final ActivityIDArgumentResolver activityIDArgumentResolver;

  public WebMvcConfig(
      ClubIdArgumentResolver clubIdArgumentResolver,
      ArticleIdArgumentResolver articleIdArgumentResolver,
      PublicationIdArgumentResolver publicationIdArgumentResolver,
      ActivityIDArgumentResolver activityIDArgumentResolver) {
    this.clubIdArgumentResolver = clubIdArgumentResolver;
    this.articleIdArgumentResolver = articleIdArgumentResolver;
    this.publicationIdArgumentResolver = publicationIdArgumentResolver;
    this.activityIDArgumentResolver = activityIDArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(clubIdArgumentResolver);
    resolvers.add(articleIdArgumentResolver);
    resolvers.add(publicationIdArgumentResolver);
    resolvers.add(activityIDArgumentResolver);
  }

  @Bean
  public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
    return resolver ->
        resolver.setFallbackPageable(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id")));
  }
}
