package com.leaguevoice.api.dtos;

public record LeagueMatchParticipantDTO(
        String teamId,
        String championId,
        String summonerId
) {
}
