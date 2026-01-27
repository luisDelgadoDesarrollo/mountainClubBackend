package luis.delgado.clubmontana.backend.domain.model;

import java.util.Map;
import luis.delgado.clubmontana.backend.domain.model.enums.MailType;

public record MailMessage(String to, MailType type, Map<String, Object> variables) {}
