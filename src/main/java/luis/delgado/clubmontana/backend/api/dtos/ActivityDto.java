package luis.delgado.clubmontana.backend.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ActivityDto(
    @NotBlank String title,
    String slug,
    String description,
    @NotNull BigDecimal affiliatePrice,
    BigDecimal noAffiliatePrice,
    @NotNull LocalDateTime startDate,
    LocalDateTime endDate,
    List<String> imagesPath) {}
