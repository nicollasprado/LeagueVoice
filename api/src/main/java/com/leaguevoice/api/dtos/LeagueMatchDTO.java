package com.leaguevoice.api.dtos;

import java.util.List;

public record LeagueMatchDTO(
        Integer gameId,
        String gamemode,
        List<LeagueMatchParticipantDTO> participants
) {
}
