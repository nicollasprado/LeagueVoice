package com.leaguevoice.api.client;

import com.leaguevoice.api.dtos.LeagueGetUserInfoDTO;
import com.leaguevoice.api.dtos.LeagueMatchDTO;
import com.leaguevoice.api.dtos.RiotAccountGetDTO;
import com.leaguevoice.api.services.exceptions.LeagueInfoNotFoundException;
import com.leaguevoice.api.services.exceptions.LeagueMatchNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class RiotApiClient {
    private final String RIOT_AMERICAS_ACCOUNT_API_URL = "https://americas.api.riotgames.com/riot/account/v1";
    private final String RIOT_BR_API_URL = "https://br1.api.riotgames.com/lol";

    @Value("${api.riot.dev.apikey}")
    private String API_KEY;

    public String getUserPuuidByNameTag(String leagueNameTag){
        String[] leagueNameTagSplits = leagueNameTag.split("#");

        String apiUrl = UriComponentsBuilder.fromUriString(RIOT_AMERICAS_ACCOUNT_API_URL + "/accounts/by-riot-id/{leagueUser}/{leagueTag}")
                .queryParam("api_key", API_KEY)
                .buildAndExpand(leagueNameTagSplits[0], leagueNameTagSplits[1])
                .toUriString();

        try{
            RiotAccountGetDTO response = new RestTemplate().getForObject(apiUrl, RiotAccountGetDTO.class);

            return response.puuid();
        } catch (HttpClientErrorException.NotFound e) {
            throw new LeagueInfoNotFoundException(leagueNameTag);
        }
    }

    public LeagueGetUserInfoDTO getLeagueInfo(String leagueUserPuuid){
        String apiUrl = UriComponentsBuilder.fromUriString(RIOT_BR_API_URL + "/league/v4/entries/by-puuid/{puuid}")
                .queryParam("api_key", API_KEY)
                .buildAndExpand(leagueUserPuuid)
                .toUriString();

        try{
            ResponseEntity<List<LeagueGetUserInfoDTO>> response = new RestTemplate().exchange(apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });

            return response.getBody().getFirst();
        }catch (HttpClientErrorException.NotFound e){
            throw new LeagueInfoNotFoundException("player not found");
        }
    }

    public LeagueMatchDTO getUserActiveMatchInfo(String leagueUserPuuid){
        String apiUrl = UriComponentsBuilder.fromUriString(RIOT_BR_API_URL + "/spectator/v5/active-games/by-summoner/{puuid}")
                .queryParam("api_key", API_KEY)
//                .buildAndExpand(leagueUserPuuid)
                .buildAndExpand("5aTgTXEx1Qh4gJWHovRP8JKjV-ne4r5wg5BhvnHdtso8ALeibybrqI8fT4eQnbZl7Hz-Y43m7BmLSg")
                .toUriString();

        return new RestTemplate().getForObject(apiUrl, LeagueMatchDTO.class);
    }



}
