package com.leaguevoice.api.repositories;

import com.leaguevoice.api.models.ActiveMatch;
import com.leaguevoice.api.models.MatchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchUserRepository extends JpaRepository<MatchUser, UUID> {
    Optional<MatchUser> findByActiveMatch(ActiveMatch activeMatch);

    Optional<List<MatchUser>> findAllByActiveMatch(ActiveMatch activeMatch);

    @Query(value = "SELECT a.user.leaguePuuid FROM MatchUser a WHERE a.discordId = :discordId")
    Optional<MatchUser> findByDiscordId(@Param("discordId") String discordId);

}
