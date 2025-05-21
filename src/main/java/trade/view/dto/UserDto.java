package trade.view.dto;

import trade.view.entity.UserRole;

import java.time.Instant;

public record UserDto(
        Long       id,
        String     username,
        UserRole   role,
        Instant    createdAt,
        Instant    updatedAt
) {}