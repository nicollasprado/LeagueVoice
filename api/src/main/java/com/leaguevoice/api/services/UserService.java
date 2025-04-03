package com.leaguevoice.api.services;

import com.leaguevoice.api.client.RiotApiClient;
import com.leaguevoice.api.dtos.LeagueGetUserInfoDTO;
import com.leaguevoice.api.dtos.UserCreateDTO;
import com.leaguevoice.api.dtos.UserCreateResponseDTO;
import com.leaguevoice.api.dtos.UserGetDTO;
import com.leaguevoice.api.services.exceptions.LeagueInfoNotFoundException;
import com.leaguevoice.api.services.exceptions.UserNotFoundException;
import com.leaguevoice.api.models.User;
import com.leaguevoice.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiotApiClient riotApiClient;


    public UserCreateResponseDTO createUnique(UserCreateDTO data){
        User newUser = userRepository.save(new User(
                data.leagueId(),
                data.leaguePuuid(),
                data.discordId()
        ));

        return new UserCreateResponseDTO(
                newUser.getId(),
                newUser.getLeagueId(),
                newUser.getLeaguePuuid(),
                newUser.getDiscordId(),
                newUser.getCreatedAt()
        );
    }

    public UserGetDTO getUniqueByDiscordId(String discordId){
        Optional<User> foundUser = userRepository.findByDiscordId(discordId);

        if(foundUser.isEmpty()){
            throw new UserNotFoundException(discordId);
        }

        User user = foundUser.get();
        return new UserGetDTO(
                user.getId(),
                user.getLeagueId(),
                user.getLeaguePuuid(),
                user.getDiscordId(),
                user.getCreatedAt()
        );
    }

    public LeagueGetUserInfoDTO getLeagueInfoByDiscordId(String discordId){
        String leagueUserPuuid = this.getUniqueByDiscordId(discordId).leaguePuuid();
        LeagueGetUserInfoDTO infoDTO = riotApiClient.getLeagueInfo(leagueUserPuuid);

        if(infoDTO.tier().isBlank()){
            throw new LeagueInfoNotFoundException(discordId);
        }

        int totalGames = infoDTO.wins() + infoDTO.losses();
        double winrate = ((double) infoDTO.wins() / totalGames);
        int winratePercentage = (int) (winrate * 100);

        return new LeagueGetUserInfoDTO(
                infoDTO.tier(),
                infoDTO.rank(),
                infoDTO.wins(),
                infoDTO.losses(),
                winratePercentage
        );
    }

}
