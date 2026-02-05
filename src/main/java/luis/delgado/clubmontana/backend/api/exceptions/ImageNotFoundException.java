package luis.delgado.clubmontana.backend.api.exceptions;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException(String url) {
    super("Image " + url + " not found ");
  }
}
