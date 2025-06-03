package trade.view.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import trade.view.entity.AssetType;

import java.util.Map;

public record AssetRequest(
        @NotBlank
        String symbol,

        @NotNull
        AssetType type,

        @NotNull
        Integer amount,

        Map<String,Object> parameters
) {}