package com.leaguevoice.api.services.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String discordId) {
        super("User with discordId: " + discordId + " not found in database.");
    }
}
