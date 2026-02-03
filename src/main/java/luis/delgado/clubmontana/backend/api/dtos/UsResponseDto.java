package luis.delgado.clubmontana.backend.api.dtos;

import java.util.List;

public record UsResponseDto(Long clubId, String text, List<String> images) {}
