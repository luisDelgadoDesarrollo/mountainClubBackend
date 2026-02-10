package luis.delgado.clubmontana.backend.application.services;

import java.text.Normalizer;
import luis.delgado.clubmontana.backend.domain.services.SlugFactory;
import org.springframework.stereotype.Service;

@Service
public class SlugFactoryImpl implements SlugFactory {

  // todo hacer las llamadas que correspondan para comprobar que el slug es unico
  @Override
  public String makeSlug(String text) {
    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

    String slug =
        normalized
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "") // quita acentos
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "-") // solo letras y números
            .replaceAll("(^-|-$)", "");
    return slug;
  }
}
