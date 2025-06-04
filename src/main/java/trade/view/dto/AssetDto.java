package trade.view.dto;

import trade.view.entity.AssetType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record AssetDto(
        Long          id,
        Long          userId,
        String        symbol,
        BigDecimal amount,
        AssetType     type,
        Map<String,Object> parameters,
        Instant       createdAt,
        Instant       updatedAt
) {}