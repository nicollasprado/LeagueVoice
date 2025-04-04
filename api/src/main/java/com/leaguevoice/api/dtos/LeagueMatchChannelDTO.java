package com.leaguevoice.api.dtos;

import java.util.List;

public record LeagueMatchChannelDTO(
        Long gameId,
        String gameQueueConfigId,
        List<LeagueMatchParticipantDTO> participants,
        String channelId
) {
}
