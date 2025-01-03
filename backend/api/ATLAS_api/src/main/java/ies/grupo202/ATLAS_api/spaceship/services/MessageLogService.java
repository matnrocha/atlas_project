package ies.grupo202.ATLAS_api.spaceship.services;

import ies.grupo202.ATLAS_api.spaceship.entities.MessageLog;
import ies.grupo202.ATLAS_api.spaceship.entities.Spaceship;
import ies.grupo202.ATLAS_api.spaceship.repositories.MessageLogRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.SpaceshipRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageLogService {

    private MessageLogRepository messageLogRepository;
    private SpaceshipRepository spaceshipRepository;

    // Create a default message log on init
    @PostConstruct
    public void init() {
        createDefaultMessageLog();
    }

    public void createDefaultMessageLog() {
        if (messageLogRepository.count() == 0) {
            MessageLog messageLog = new MessageLog();
            messageLog.setSpaceship(spaceshipRepository.findAll().get(0));
            messageLogRepository.save(messageLog);
        }
    }

    public MessageLog createMessageLog(Long spaceship_id, MessageLog messageLog) {
        Spaceship spaceship = getSpaceshipById(spaceship_id);
        messageLog.setSpaceship(spaceship);
        return messageLogRepository.save(messageLog);
    }

    public Spaceship getSpaceshipById(Long id) {
        Optional<Spaceship> spaceship = spaceshipRepository.findById(id);
        return spaceship.get();
    }

    public MessageLog getMessageLogById(Long id) {
        Optional<MessageLog> messageLog = messageLogRepository.findById(id);
        return messageLog.get();
    }

    public List<MessageLog> getAllMessageLogs() {
        return messageLogRepository.findAll();
    }

    public void deleteMessageLog(Long id) {
        messageLogRepository.deleteById(id);
    }

    // "_Id" is a reserved keyword in JPA used to reference the id of the entity
    public List<MessageLog> getMessageLogsBySpaceshipId(Long spaceshipId) {
        return messageLogRepository.findBySpaceship_Id(spaceshipId);
    }
}
