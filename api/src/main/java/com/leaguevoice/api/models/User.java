package com.leaguevoice.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = User.TABLE_NAME)
@Table(name = User.TABLE_NAME)
@Data
@NoArgsConstructor
public class User {
    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false, updatable = false, insertable = false)
    private UUID id;

    @Column(name = "league_id", nullable = false, unique = true)
    @NotBlank
    private String leagueId;

    @Column(name = "league_puuid", nullable = false, unique = true)
    @NotBlank
    private String leaguePuuid;

    @Column(name = "discord_id", nullable = false, unique = true)
    @NotBlank
    private String discordId;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @OneToOne(mappedBy = "userId")
    @JsonIgnore
    private MatchUser activeMatch;


    public User(String leagueId, String leaguePuuid, String discordId){
        this.leagueId = leagueId;
        this.leaguePuuid = leaguePuuid;
        this.discordId = discordId;
    }
}
