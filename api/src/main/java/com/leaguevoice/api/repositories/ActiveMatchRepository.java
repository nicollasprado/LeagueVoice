package com.leaguevoice.api.repositories;

import com.leaguevoice.api.models.ActiveMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveMatchRepository extends JpaRepository<ActiveMatch, Long> {
}
