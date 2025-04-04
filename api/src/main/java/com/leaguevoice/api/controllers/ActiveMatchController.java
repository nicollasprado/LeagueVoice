package com.leaguevoice.api.controllers;

import com.leaguevoice.api.dtos.LeagueMatchChannelDTO;
import com.leaguevoice.api.dtos.LeagueMatchDTO;
import com.leaguevoice.api.services.ActiveMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/activeMatch")
public class ActiveMatchController {
    @Autowired
    private ActiveMatchService activeMatchService;


    @PostMapping("/{discordId}/{channelId}")
    public ResponseEntity<LeagueMatchChannelDTO> getByMatchId(@PathVariable String discordId, @PathVariable String channelId){
        LeagueMatchChannelDTO match = activeMatchService.createByDiscordId(discordId, channelId);

        URI matchUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/channel/by-discordid/{discordId}")
                .buildAndExpand(discordId)
                .toUri();

        return ResponseEntity.created(matchUri).body(match);
    }


    @GetMapping("/by-matchid/{matchId}")
    public ResponseEntity<LeagueMatchDTO> getByMatchId(@PathVariable Long matchId){
        LeagueMatchDTO match = activeMatchService.getByMatchId(matchId);

        return ResponseEntity.ok(match);
    }

    @GetMapping("/by-discordid/{discordId}")
    public ResponseEntity<LeagueMatchDTO> getByDiscordId(@PathVariable String discordId){
        LeagueMatchDTO match = activeMatchService.getByDiscordId(discordId);

        return ResponseEntity.ok(match);
    }

    @GetMapping("/channel/by-discordid/{discordId}")
    public ResponseEntity<LeagueMatchChannelDTO> getChannelByDiscordId(@PathVariable String discordId){
        LeagueMatchChannelDTO match = activeMatchService.getChannelByDiscordId(discordId);

        return ResponseEntity.ok(match);
    }
}
