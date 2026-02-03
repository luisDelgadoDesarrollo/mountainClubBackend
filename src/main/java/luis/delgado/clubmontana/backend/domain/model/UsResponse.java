package luis.delgado.clubmontana.backend.domain.model;

import java.util.List;

public record UsResponse(Long clubId, String text, List<String> images) {}
