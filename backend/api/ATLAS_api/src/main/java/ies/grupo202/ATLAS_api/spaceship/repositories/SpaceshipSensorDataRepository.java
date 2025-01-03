package ies.grupo202.ATLAS_api.spaceship.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipSensorData;

public interface SpaceshipSensorDataRepository extends JpaRepository<SpaceshipSensorData, Long> {
    List<SpaceshipSensorData> findBySpaceshipIdAndTimestampBetween(Long spaceshipId, LocalDateTime start, LocalDateTime end);


    @Query("SELECT v.id FROM SpaceshipSensorData v ORDER BY v.timestamp ASC")
    List<Long> findOldestIdsToRemove(Pageable pageable);

}