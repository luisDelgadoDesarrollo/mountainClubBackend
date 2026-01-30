package luis.delgado.clubmontana.backend.api.dtos;

import java.util.List;

public record UsRequestDto(String text, List<ImageRequestDto> images) {}
