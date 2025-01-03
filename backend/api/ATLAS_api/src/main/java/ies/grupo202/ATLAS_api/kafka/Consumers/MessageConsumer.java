package ies.grupo202.ATLAS_api.kafka.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ies.grupo202.ATLAS_api.spaceship.services.MessageService;
import ies.grupo202.ATLAS_api.kafka.Entities.MessageMsg;
import ies.grupo202.ATLAS_api.spaceship.entities.Message;
import ies.grupo202.ATLAS_api.spaceship.entities.Alert;

@Service
public class MessageConsumer {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "messages", groupId = "ATLAS", properties = "spring.json.value.default.type=ies.grupo202.ATLAS_api.kafka.Entities.MessageMsg")
    public void consume(MessageMsg message) {
        // Emit system message to WebSocket topic
        System.out.println("Consumed message: " + message);
        messagingTemplate.convertAndSend("/topic/messages", message);

        // Save message
        Message createdMessage = messageService.createMessageByMessage(message);

        // Check response time status
        if (createdMessage.getSender() == null) {
            Alert alert = messageService.checkResponseTimeStatus(createdMessage.getId(),
                    createdMessage.getMessage());

            if (alert != null) {
                messagingTemplate.convertAndSend("/topic/alerts", alert);
            }
        }

    }
}