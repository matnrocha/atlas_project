package ies.grupo202.ATLAS_api.spaceship.repositories;

import ies.grupo202.ATLAS_api.spaceship.entities.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {

    List<MessageLog> findBySpaceship_Id(Long spaceshipId);
}
