package luis.delgado.clubmontana.backend.core.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import luis.delgado.clubmontana.backend.core.annotations.ValidSpanishNif;

public class SpanishNifValidator implements ConstraintValidator<ValidSpanishNif, String> {

  private static final String CONTROL_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return true;
    }

    String nif = value.trim().toUpperCase();
    if (!nif.matches("^([XYZ]\\d{7}|\\d{8})[A-Z]$")) {
      return false;
    }

    char prefix = nif.charAt(0);
    String numericPart =
        switch (prefix) {
          case 'X' -> "0" + nif.substring(1, 8);
          case 'Y' -> "1" + nif.substring(1, 8);
          case 'Z' -> "2" + nif.substring(1, 8);
          default -> nif.substring(0, 8);
        };

    int number = Integer.parseInt(numericPart);
    char expectedLetter = CONTROL_LETTERS.charAt(number % 23);
    char actualLetter = nif.charAt(nif.length() - 1);

    return expectedLetter == actualLetter;
  }
}
