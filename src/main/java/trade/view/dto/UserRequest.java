package trade.view.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import trade.view.entity.UserRole;

public record UserRequest(
        @NotBlank
        String username,

        @NotNull
        Long telegramId,

        @NotNull
        UserRole role
) {}