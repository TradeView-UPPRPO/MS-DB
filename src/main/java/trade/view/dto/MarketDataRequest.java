package trade.view.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record MarketDataRequest(
        @NotBlank String symbol,
        @NotBlank String source,
        @NotNull Map<String,Object> data,
        BigDecimal price,
        BigDecimal volume,
        BigDecimal changePct,
        Instant fetchedAt
) {}