package com.leaguevoice.api.dtos;

public record LeagueGetUserInfoDTO(
        String tier,
        String rank,
        Integer wins,
        Integer losses
) {
}
