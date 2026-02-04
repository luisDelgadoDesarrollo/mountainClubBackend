package luis.delgado.clubmontana.backend.api.exceptions;

public class PdfStorageException extends RuntimeException {

  public PdfStorageException(Long clubId, String message) {
    super(message + " " + clubId);
  }
}
