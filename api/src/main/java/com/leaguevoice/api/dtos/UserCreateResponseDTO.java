package com.leaguevoice.api.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreateResponseDTO(
        UUID id,
        String leagueId,
        String leaguePuuid,
        String discordId,
        LocalDate createdAt
) {
}
