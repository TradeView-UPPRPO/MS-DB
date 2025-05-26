package trade.view.dto;

import java.time.Instant;
import java.util.Map;

public record AnalysisDataDto(
        Long id,
        String symbol,
        Map<String,Object> analysis,
        Instant createdAt,
        Instant updatedAt
) {}