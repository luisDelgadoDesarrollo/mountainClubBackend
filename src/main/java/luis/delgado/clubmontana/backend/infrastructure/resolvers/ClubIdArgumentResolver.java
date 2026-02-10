package luis.delgado.clubmontana.backend.infrastructure.resolvers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.ClubId;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.ClubSlugResolver;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class ClubIdArgumentResolver implements HandlerMethodArgumentResolver {

  private final ClubSlugResolver clubSlugResolver;

  public ClubIdArgumentResolver(ClubSlugResolver clubSlugResolver) {
    this.clubSlugResolver = clubSlugResolver;
  }

  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(ClubId.class)
        && parameter.getParameterType().equals(Long.class);
  }

  @Override
  public Object resolveArgument(
      @NonNull MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    @SuppressWarnings("unchecked")
    Map<String, String> uriTemplateVars =
        (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    String clubSlug = uriTemplateVars.get("club");

    return clubSlugResolver.resolve(clubSlug);
  }
}
