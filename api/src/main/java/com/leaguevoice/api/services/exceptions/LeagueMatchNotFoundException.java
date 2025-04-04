package com.leaguevoice.api.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LeagueMatchNotFoundException extends RuntimeException{
    public LeagueMatchNotFoundException(String matchId) {
        super("Match of id: " + matchId + " not found.");
    }

    public LeagueMatchNotFoundException(){
        super();
    }
}
