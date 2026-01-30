package luis.delgado.clubmontana.backend.api.exceptions;

public class UnsupportedImageTypeException extends RuntimeException {
  public UnsupportedImageTypeException(String mime) {
    super("Unsupported image type: " + mime);
  }
}
