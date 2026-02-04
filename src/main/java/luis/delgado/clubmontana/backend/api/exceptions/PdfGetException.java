package luis.delgado.clubmontana.backend.api.exceptions;

public class PdfGetException extends RuntimeException {

  public PdfGetException(Long clubId, String message) {
    super(message + " " + clubId);
  }
}
