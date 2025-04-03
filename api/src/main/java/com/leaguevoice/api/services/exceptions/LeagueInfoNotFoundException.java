package com.leaguevoice.api.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LeagueInfoNotFoundException extends RuntimeException {
  public LeagueInfoNotFoundException(String credential) {
    super("League info of credential: " + credential + " not found.");
  }
}
