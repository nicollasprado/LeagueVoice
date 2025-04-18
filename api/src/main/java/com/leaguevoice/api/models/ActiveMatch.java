package com.leaguevoice.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = ActiveMatch.TABLE_NAME)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveMatch {
    public static final String TABLE_NAME = "active_match";

    @Id
    @Column(name = "game_id", nullable = false, unique = true, updatable = false)
    @NotNull
    private Long id;

    @Column(name = "queue_type_id", nullable = false, updatable = false)
    @NotBlank
    private String queueTypeId;

    @OneToMany(mappedBy = "activeMatchId", cascade = CascadeType.ALL)
    private List<MatchUser> usersInMatch;

    @Column(name = "channel_id", nullable = false, unique = true)
    @NotBlank
    private String channelId;


    public ActiveMatch(Long id, String queueTypeId, String channelId) {
        this.id = id;
        this.queueTypeId = queueTypeId;
        this.channelId = channelId;
    }

}
