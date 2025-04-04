package com.leaguevoice.api.models;

import jakarta.persistence.*;
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
    private User userId;

    @ManyToOne
    @JoinColumn(name = "active_match_id")
    private ActiveMatch activeMatchId;


    public MatchUser(User userId, ActiveMatch activeMatchId) {
        this.userId = userId;
        this.activeMatchId = activeMatchId;
    }
}
