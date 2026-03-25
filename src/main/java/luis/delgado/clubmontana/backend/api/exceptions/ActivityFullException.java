package luis.delgado.clubmontana.backend.api.exceptions;

public class ActivityFullException extends RuntimeException {
  public ActivityFullException(Long activityId) {
    super("La actividad con id " + activityId + " ha alcanzado el maximo de participantes");
  }
}
