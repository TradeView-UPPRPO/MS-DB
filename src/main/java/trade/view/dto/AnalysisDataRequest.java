package trade.view.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record AnalysisDataRequest(
        @NotBlank String symbol,
        @NotNull  Map<String,Object> analysis
) {}
