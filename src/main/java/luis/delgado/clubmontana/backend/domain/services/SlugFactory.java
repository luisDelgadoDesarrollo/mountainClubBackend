package luis.delgado.clubmontana.backend.domain.services;

import java.util.function.Function;

public interface SlugFactory {
  String makeSlug(String text, Function<String, Boolean> existsSlug);
}
