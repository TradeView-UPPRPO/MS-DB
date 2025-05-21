package trade.view.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record MarketDataDto(
        Long id,
        String symbol,
        String source,
        Map<String,Object> data,
        BigDecimal price,
        BigDecimal volume,
        BigDecimal changePct,
        Instant fetchedAt,
        Instant createdAt,
        Instant updatedAt
) {}