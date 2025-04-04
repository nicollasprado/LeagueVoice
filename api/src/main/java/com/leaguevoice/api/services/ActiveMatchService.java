package com.leaguevoice.api.services;

import com.leaguevoice.api.client.RiotApiClient;
import com.leaguevoice.api.dtos.LeagueMatchChannelDTO;
import com.leaguevoice.api.dtos.LeagueMatchDTO;
import com.leaguevoice.api.dtos.LeagueMatchParticipantDTO;
import com.leaguevoice.api.dtos.LeagueSummonerDTO;
import com.leaguevoice.api.models.ActiveMatch;
import com.leaguevoice.api.models.MatchUser;
import com.leaguevoice.api.models.User;
import com.leaguevoice.api.repositories.ActiveMatchRepository;
import com.leaguevoice.api.services.exceptions.LeagueMatchNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActiveMatchService {
    @Autowired
    private ActiveMatchRepository activeMatchRepository;

    @Autowired
    private MatchUserService matchUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RiotApiClient riotApiClient;


    @Transactional
    public LeagueMatchChannelDTO createByDiscordId(String discordId, String channelId){
        LeagueMatchDTO match = this.getByDiscordId(discordId);

        ActiveMatch channelMatch = activeMatchRepository.saveAndFlush(new ActiveMatch(
                match.gameId(),
                match.gameQueueConfigId(),
                channelId
        ));

        User user = userService.getRawUniqueByDiscordId(discordId);
        LeagueSummonerDTO summoner = riotApiClient.getSummonerInfoByPuuid(user.getLeaguePuuid());

        String teamId;
        String championId;
        for(LeagueMatchParticipantDTO player : match.participants()){
            if(player.summonerId().equals(summoner.summonerId())){
                teamId = player.teamId();
                championId = player.championId();

                MatchUser matchUser = new MatchUser(
                        user,
                        channelMatch,
                        teamId,
                        championId,
                        summoner.summonerId()
                );

                channelMatch.setUsersInMatch(List.of(matchUser));
            }
        }

        activeMatchRepository.save(channelMatch);

        return new LeagueMatchChannelDTO(
                channelMatch.getId(),
                channelMatch.getQueueTypeId(),
                match.participants(),
                channelId
        );
    }


    public LeagueMatchDTO getByMatchId(Long matchId){
        Optional<ActiveMatch> found = activeMatchRepository.findById(matchId);

        if(found.isEmpty()){
            throw new LeagueMatchNotFoundException(matchId.toString());
        }

        ActiveMatch match = found.get();
        List<LeagueMatchParticipantDTO> matchParticipants = matchUserService.getMatchParticipantsByActiveMatch(match);

        return new LeagueMatchDTO(
                match.getId(),
                match.getQueueTypeId(),
                matchParticipants
        );
    }


    // fetch in riot api
    public LeagueMatchDTO getByDiscordId(String discordId){
        String leagueUserPuuid = userService.getUniqueByDiscordId(discordId).leaguePuuid();
        return riotApiClient.getUserActiveMatchInfo(leagueUserPuuid);
    }


    public LeagueMatchChannelDTO getChannelByDiscordId(String discordId){
        MatchUser matchUser = matchUserService.getMatchUserByDiscordId(discordId);
        ActiveMatch activeMatch = matchUser.getActiveMatch();
        List<LeagueMatchParticipantDTO> activeMatchParticipants = matchUserService.getMatchParticipantsByActiveMatch(activeMatch);

        return new LeagueMatchChannelDTO(
                activeMatch.getId(),
                activeMatch.getQueueTypeId(),
                activeMatchParticipants,
                activeMatch.getChannelId()
        );
    }


}
