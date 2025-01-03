package ies.grupo202.ATLAS_api.spaceship.repositories;

import ies.grupo202.ATLAS_api.spaceship.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.message LIKE %:substring%")
    List<Message> findByMessageContaining(@Param("substring") String substring);

    List<Message> findBySender_Name(String senderName);

    List<Message> findByMessageLog_Id(Long messageLogId);

    Message findTopByMessageLog_IdOrderByIdDesc(Long messageLogId);

    @Query("SELECT v.id FROM Message v ORDER BY v.timestamp ASC")
    List<Long> findOldestIdsToRemove(Pageable pageable);

}
