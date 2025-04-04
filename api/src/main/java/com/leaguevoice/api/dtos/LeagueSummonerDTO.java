package com.leaguevoice.api.dtos;

public record LeagueSummonerDTO(
        String summonerId,
        String puuid,
        Integer profileIconId,
        Integer summonerLevel
) {
}
