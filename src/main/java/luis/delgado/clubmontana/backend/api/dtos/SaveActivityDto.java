package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaveActivityDto(
    @NotBlank String title,
    String description,
    @NotNull BigDecimal affiliatePrice,
    BigDecimal noAffiliatePrice,
    @NotNull LocalDateTime startDate,
    LocalDateTime endDate,
    List<ImageRequestDto> images) {}
