package com.leaguevoice.api.dtos;

public record UserCreateDTO(
        String leagueId,
        String leaguePuuid,
        String discordId
) {
}
