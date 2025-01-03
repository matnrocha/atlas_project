package ies.grupo202.ATLAS_api.kafka.Consumers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import ies.grupo202.ATLAS_api.astronaut.entities.VitalSigns;
import ies.grupo202.ATLAS_api.astronaut.services.AstronautService;
import ies.grupo202.ATLAS_api.kafka.Entities.VitalSignsMsg;
import ies.grupo202.ATLAS_api.spaceship.entities.Alert;

@Service
public class VitalSignsConsumer {

    @Autowired
    private AstronautService astronautService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "vital-signs", groupId = "ATLAS", properties = "spring.json.value.default.type=ies.grupo202.ATLAS_api.kafka.Entities.VitalSignsMsg")
    public void consume(VitalSignsMsg data) {
        astronautService.cleanupOldVitalSigns();

        messagingTemplate.convertAndSend("/topic/vitals", data);

        // Extract astronaut ID and values
        Long astronautId = Long.valueOf(data.getAstronaut_id());
        List<Double> values = data.getDoubles();
        LocalDateTime timestamp = data.getLastUpdate();

        // Save vital signs

        VitalSigns vitalSigns = astronautService.createVitalSignsByMessage(astronautId, values, timestamp); // fix

        // Check astronaut status for any anomalies
        List<Alert> alerts = astronautService.checkAstronautStatus(astronautId, vitalSigns);

        // Retrieve alerts generated during status check and send them via WebSocket
        for (Alert alert : alerts) {
            // System.out.println("new alert generated: " + alert);
            messagingTemplate.convertAndSend("/topic/alerts", alert);
        }

        // Debugging log
        // System.out.println("Processed vital signs: " + data);
    }
}
