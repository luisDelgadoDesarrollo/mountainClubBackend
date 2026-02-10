package luis.delgado.clubmontana.backend.infrastructure.resolvers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.PublicationId;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.PublicationSlugResolver;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class PublicationIdArgumentResolver implements HandlerMethodArgumentResolver {

  private final PublicationSlugResolver publicationSlugResolver;

  public PublicationIdArgumentResolver(PublicationSlugResolver publicationSlugResolver) {
    this.publicationSlugResolver = publicationSlugResolver;
  }

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    return parameter.hasParameterAnnotation(PublicationId.class)
        && parameter.getParameterType().equals(Long.class);
  }

  @Override
  public @Nullable Object resolveArgument(
      @NonNull MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      @NonNull NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory)
      throws Exception {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    @SuppressWarnings("unchecked")
    Map<String, String> uriTemplateVars =
        (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String publicationSlug = uriTemplateVars.get("publication");

    return publicationSlugResolver.resolve(publicationSlug);
  }
}
