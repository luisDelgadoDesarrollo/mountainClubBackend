package luis.delgado.clubmontana.backend.application.services;

import java.text.Normalizer;
import java.util.function.Function;
import luis.delgado.clubmontana.backend.domain.services.SlugFactory;
import org.springframework.stereotype.Service;

@Service
public class SlugFactoryImpl implements SlugFactory {

  @Override
  public String makeSlug(String text, Function<String, Boolean> existsSlug) {
    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
    int counter = 1;

    String slug =
        normalized
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "") // quita acentos
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-") // solo letras y números
            .replaceAll("(^-|-$)", "");

    while (existsSlug.apply(slug)) {
      slug = slug + "-" + counter++;
    }
    return slug;
  }
}
