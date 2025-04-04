package com.leaguevoice.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
public class MatchUser {
    public static final String TABLE_NAME = "match_user";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "relation_id", unique = true, insertable = false, updatable = false, nullable = false)
    private UUID relationId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "active_match_id")
    private ActiveMatch activeMatch;

    @Column(name = "team_id", nullable = false, updatable = false)
    @NotBlank
    private String teamId;

    @Column(name = "champion_id", nullable = false, updatable = false)
    @NotBlank
    private String championId;

    @Column(name = "summoner_id", nullable = false, updatable = false)
    @NotBlank
    private String summonerId;


    public MatchUser(User user, ActiveMatch activeMatch, String teamId, String championId, String summonerId) {
        this.user = user;
        this.activeMatch = activeMatch;
        this.teamId = teamId;
        this.championId = championId;
        this.summonerId = summonerId;
    }
}
