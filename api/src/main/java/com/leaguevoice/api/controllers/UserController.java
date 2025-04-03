package com.leaguevoice.api.controllers;

import com.leaguevoice.api.dtos.LeagueGetUserInfoDTO;
import com.leaguevoice.api.dtos.UserCreateDTO;
import com.leaguevoice.api.dtos.UserCreateResponseDTO;
import com.leaguevoice.api.dtos.UserGetDTO;
import com.leaguevoice.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserCreateResponseDTO> create(@RequestBody UserCreateDTO data){
        UserCreateResponseDTO createdUser = userService.createUnique(data);

        URI newUserURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{discordId}")
                .buildAndExpand(createdUser.discordId())
                .toUri();

        return ResponseEntity.created(newUserURI).body(createdUser);
    }

    @GetMapping("/user/{discordId}")
    public ResponseEntity<UserGetDTO> getUser(@PathVariable String discordId){
        UserGetDTO userDTO = userService.getUniqueByDiscordId(discordId);

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/user/leagueInfo/{discordId}")
    public ResponseEntity<LeagueGetUserInfoDTO> getLeagueInfo(@PathVariable String discordId){
        LeagueGetUserInfoDTO info = userService.getLeagueInfoByDiscordId(discordId);

        return ResponseEntity.ok(info);
    }
}
