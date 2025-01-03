package ies.grupo202.ATLAS_api.spaceship.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ies.grupo202.ATLAS_api.spaceship.entities.Alert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("SELECT v.id FROM Alert v ORDER BY v.timestamp ASC")
    List<Long> findOldestIdsToRemove(Pageable pageable);
}