package ies.grupo202.ATLAS_api.kafka.Consumers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ies.grupo202.ATLAS_api.spaceship.services.SpaceshipService;
import ies.grupo202.ATLAS_api.kafka.Entities.SensorData;
import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipSensorData;
import ies.grupo202.ATLAS_api.spaceship.entities.Alert;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class SensorDataConsumer {

    @Autowired
    private SpaceshipService spaceshipService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "sensor-data", groupId = "ATLAS", properties = "spring.json.value.default.type=ies.grupo202.ATLAS_api.kafka.Entities.SensorData")
    public void consume(SensorData data) {
        spaceshipService.cleanupOldSensorData();

        // Emit sensor data to WebSocket topic
        messagingTemplate.convertAndSend("/topic/sensor", data);
        
        //Extract spaceship ID and values
        Long spaceshipId = Long.valueOf(data.getSpaceship_id());
        List<Double> values = data.getDoubles();
        LocalDateTime timestamp = data.getTimestamp();

        // Save sensor data
        SpaceshipSensorData sensorData = spaceshipService.createSensorDataByMessage(spaceshipId, values, timestamp);
        
        // Check spaceship status for any anomalies
        List<Alert> wow = spaceshipService.checkSpaceshipStatus(spaceshipId, sensorData);
        
        // Retrieve alerts generated during status check and send them via WebSocket
        for (Alert alert : wow) {
                System.out.println("new alert generated: " + alert);
                messagingTemplate.convertAndSend("/topic/alerts", alert);
        }

        // Debugging log
        System.out.println("Processed sensor data: " + data);
    }
}
