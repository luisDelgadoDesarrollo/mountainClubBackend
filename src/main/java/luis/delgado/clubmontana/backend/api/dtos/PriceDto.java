package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record PriceDto(@NotBlank String title, BigDecimal price) {}
