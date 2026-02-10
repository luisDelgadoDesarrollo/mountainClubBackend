package luis.delgado.clubmontana.backend.infrastructure.resolvers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import luis.delgado.clubmontana.backend.core.annotations.ActivityId;
import luis.delgado.clubmontana.backend.domain.services.slugResolvers.ActivitySlugResolver;
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
public class ActivityIDArgumentResolver implements HandlerMethodArgumentResolver {

  private final ActivitySlugResolver activitySlugResolver;

  public ActivityIDArgumentResolver(ActivitySlugResolver activitySlugResolver) {
    this.activitySlugResolver = activitySlugResolver;
  }

  @Override
  public boolean supportsParameter(@NonNull MethodParameter parameter) {
    return parameter.hasParameterAnnotation(ActivityId.class)
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
    String activitySlug = uriTemplateVars.get("activity");
    return activitySlugResolver.resolve(activitySlug);
  }
}
