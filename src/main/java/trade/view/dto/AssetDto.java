package trade.view.dto;

import trade.view.entity.AssetType;

import java.time.Instant;
import java.util.Map;

public record AssetDto(
        Long          id,
        Long          userId,
        String        symbol,
        AssetType     type,
        Map<String,Object> parameters,
        Instant       createdAt,
        Instant       updatedAt
) {}