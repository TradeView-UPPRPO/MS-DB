package trade.view.dto;

import trade.view.entity.UserRole;

import java.time.Instant;

public record UserDto(
        Long id,
        String username,
        Long telegramId,
        UserRole role,
        Instant createdAt,
        Instant updatedAt
) {
}