package luis.delgado.clubmontana.backend.api.exceptions;

public class SlugNotFoundException extends RuntimeException {
  public SlugNotFoundException(String slug, Class<?> clazz) {
    super("Slug " + slug + " not found for class " + clazz.getSimpleName());
  }
}
