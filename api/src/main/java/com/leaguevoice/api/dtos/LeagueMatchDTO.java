package com.leaguevoice.api.dtos;

import java.util.List;

public record LeagueMatchDTO(
        Long gameId,
        String gameQueueConfigId,
        List<LeagueMatchParticipantDTO> participants
) {
}
