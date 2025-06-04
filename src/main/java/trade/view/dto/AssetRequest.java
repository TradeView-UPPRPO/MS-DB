package trade.view.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import trade.view.entity.AssetType;

import java.math.BigDecimal;
import java.util.Map;

public record AssetRequest(
        @NotBlank
        String symbol,

        @NotNull
        AssetType type,

        @NotNull
        @DecimalMin(value = "0.00000001", message = "Amount must be positive and at least 0.00000001")
        BigDecimal amount,

        Map<String,Object> parameters
) {}