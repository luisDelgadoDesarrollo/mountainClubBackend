package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UsRequestDto(@NotBlank String text, List<ImageRequestDto> images) {}
