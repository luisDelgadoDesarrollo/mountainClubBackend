package luis.delgado.clubmontana.backend.infrastructure.resolvers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.ArticleId;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.ArticleSlugResolver;
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
public class ArticleIdArgumentResolver implements HandlerMethodArgumentResolver {

  private final ArticleSlugResolver articleSlugResolver;

  public ArticleIdArgumentResolver(ArticleSlugResolver articleSlugResolver) {
    this.articleSlugResolver = articleSlugResolver;
  }

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    return parameter.hasParameterAnnotation(ArticleId.class)
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
    String articleSlug = uriTemplateVars.get("article");
    return articleSlugResolver.resolve(articleSlug);
  }
}
