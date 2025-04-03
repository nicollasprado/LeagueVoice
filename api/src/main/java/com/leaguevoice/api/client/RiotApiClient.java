package com.leaguevoice.api.client;

import com.leaguevoice.api.dtos.LeagueGetUserInfoDTO;
import com.leaguevoice.api.services.exceptions.LeagueInfoNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@NoArgsConstructor
public class RiotApiClient {
    @Value("${api.riot.dev.apikey}")
    private String API_KEY;

    public LeagueGetUserInfoDTO getLeagueInfo(String leagueUserPuuid){
        String apiUrl = UriComponentsBuilder.fromUriString("https://br1.api.riotgames.com/lol/league/v4/entries/by-puuid/{puuid}")
                .queryParam("api_key", API_KEY)
                .buildAndExpand(leagueUserPuuid)
                .toUriString();

        try{
            ResponseEntity<List<LeagueGetUserInfoDTO>> response = new RestTemplate().exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });

            return response.getBody().getFirst();
        }catch (RuntimeException e){
            throw new LeagueInfoNotFoundException(leagueUserPuuid);
        }
    }

}
