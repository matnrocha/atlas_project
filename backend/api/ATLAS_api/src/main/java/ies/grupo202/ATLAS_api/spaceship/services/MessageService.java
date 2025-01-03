package ies.grupo202.ATLAS_api.spaceship.services;

import ies.grupo202.ATLAS_api.spaceship.entities.Alert;
import ies.grupo202.ATLAS_api.spaceship.entities.Message;
import ies.grupo202.ATLAS_api.spaceship.entities.MessageLog;
import ies.grupo202.ATLAS_api.spaceship.entities.Spaceship;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.Priority;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.ShipSystem;
import ies.grupo202.ATLAS_api.spaceship.repositories.MessageLogRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.MessageRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.SpaceshipRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.AlertRepository;
import lombok.AllArgsConstructor;
import ies.grupo202.ATLAS_api.kafka.Entities.MessageMsg;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import ies.grupo202.ATLAS_api.astronaut.services.AstronautService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepository messageRepository;
    private MessageLogRepository messageLogRepository;
    private AlertRepository alertRepository;
    private AstronautService astronautService;
    private SpaceshipRepository spaceshipRepository;

    public Message createMessage(Long messageLog_id, Message message) {
        MessageLog messageLog = getMessageLogById(messageLog_id);
        message.setMessageLog(messageLog);
        return messageRepository.save(message);
    }

    // Create a msg entity from a producer-sent message
    public Message createMessageByMessage(MessageMsg messageMsg) {
        Message message = new Message();
        message.setMessageLog(getMessageLogById((long) messageMsg.getMessageLog_id()));
        System.out.println("MessageLog: " + message.getMessageLog());
        System.out.println("Message sender: " + messageMsg.getSender_id());
        if (messageMsg.getSender_id() != 0) {
            message.setSender(astronautService.getAstronautById((long) messageMsg.getSender_id()));
        } else {
            message.setSender(null);
        }
        message.setMessage(messageMsg.getMessage());
        message.setTimestamp(messageMsg.getTimestamp());
        System.out.println("Message created: " + message);
        cleanupOldMessages();
        return messageRepository.save(message);
    }

    public MessageLog getMessageLogById(Long id) {
        return messageLogRepository.findById(id).orElse(null);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message getLatestMessageByMessageLogId(Long id) {
        return messageRepository.findTopByMessageLog_IdOrderByIdDesc(id);
    }

    public List<Message> getMessagesByMessageLogId(Long id) {
        return messageRepository.findByMessageLog_Id(id);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public List<Message> getMessagesContaining(String substring) {
        return messageRepository.findByMessageContaining(substring);
    }

    public List<Message> getMessagesBySenderName(String senderName) {
        return messageRepository.findBySender_Name(senderName);
    }

    public Alert checkResponseTimeStatus(Long messageId, String responseTimeString) {

        long responseTime = Long.parseLong(responseTimeString);
        System.out.println("Response time: " + responseTime);

        Alert alert = null;
        if (responseTime > 1500) {
            alert = createAlert(messageId, Priority.HIGH, "UNRESOLVED", ShipSystem.COMMUNICATIONS,
                    "Response time too high");
        } else if (responseTime > 1200) {
            alert = createAlert(messageId, Priority.MEDIUM, "UNRESOLVED", ShipSystem.COMMUNICATIONS,
                    "Response time too high");
        } else if (responseTime > 1000) {
            alert = createAlert(messageId, Priority.LOW, "UNRESOLVED", ShipSystem.COMMUNICATIONS,
                    "Response time too high");
        }
        return alert;
    };

    // Helper function to create an alert
    private Alert createAlert(Long spaceshipId, Priority priority, String status, ShipSystem shipSystem, String cause) {
        Alert alert = new Alert();
        Spaceship spaceship = spaceshipRepository.findById((long) 1).get();
        alert.setSpaceship(spaceship);
        alert.setPriority(priority);
        alert.setStatus(status);
        alert.setShipSystem(shipSystem);
        alert.setCause(cause);
        alert.setTimestamp(LocalDateTime.now());
        alert.setResolved(false);
        alertRepository.save(alert);
        return alert;
    }

    @Transactional
    public void cleanupOldMessages() {
        int MAX_RECORDS = 300;
        int BATCH_SIZE = 100;

        long totalRecords = messageRepository.count(); 

        if (totalRecords > MAX_RECORDS) {
            Pageable pageable = PageRequest.of(0, BATCH_SIZE); 
            List<Long> oldestIds = messageRepository.findOldestIdsToRemove(pageable);

            if (!oldestIds.isEmpty()) {
                messageRepository.deleteAllByIdInBatch(oldestIds);
            }
        }
    }


}
