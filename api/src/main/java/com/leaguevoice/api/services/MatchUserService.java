package com.leaguevoice.api.services;

import com.leaguevoice.api.dtos.LeagueMatchDTO;
import com.leaguevoice.api.dtos.LeagueMatchParticipantDTO;
import com.leaguevoice.api.models.ActiveMatch;
import com.leaguevoice.api.models.MatchUser;
import com.leaguevoice.api.repositories.MatchUserRepository;
import com.leaguevoice.api.services.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchUserService {
    @Autowired
    private MatchUserRepository matchUserRepository;


    public List<LeagueMatchParticipantDTO> getMatchParticipantsByActiveMatch(ActiveMatch activeMatch){
        Optional<List<MatchUser>> matchUserListFound = matchUserRepository.findAllByActiveMatch(activeMatch);
        List<MatchUser> matchUsers = matchUserListFound.orElseGet(ArrayList::new);

        List<LeagueMatchParticipantDTO> matchUsersDtoList = new ArrayList<>();
        for(MatchUser matchUser : matchUsers){
            matchUsersDtoList.add(new LeagueMatchParticipantDTO(
                    matchUser.getTeamId(),
                    matchUser.getChampionId(),
                    matchUser.getSummonerId()
            ));
        }

        return matchUsersDtoList;
    }


    public MatchUser getMatchUserByDiscordId(String discordId){
        Optional<MatchUser> matchUserFound = matchUserRepository.findByDiscordId(discordId);

        return matchUserFound.orElseThrow(UserNotFoundException::new);
    }


}
