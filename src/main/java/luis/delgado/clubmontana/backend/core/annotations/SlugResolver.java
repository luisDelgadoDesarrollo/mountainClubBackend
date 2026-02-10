package luis.delgado.clubmontana.backend.core.annotations;

import java.lang.annotation.*;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SlugResolver {
  /** Alias for {@link Component#value}. */
  @AliasFor(annotation = Component.class)
  String value() default "";
}
