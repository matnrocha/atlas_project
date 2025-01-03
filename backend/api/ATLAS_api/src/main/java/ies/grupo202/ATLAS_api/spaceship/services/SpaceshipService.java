package ies.grupo202.ATLAS_api.spaceship.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ies.grupo202.ATLAS_api.spaceship.entities.Alert;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.Priority;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.ShipSystem;
import ies.grupo202.ATLAS_api.spaceship.entities.Spaceship;
import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipSensorData;
import ies.grupo202.ATLAS_api.spaceship.repositories.AlertRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.SpaceshipRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.SpaceshipSensorDataRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SpaceshipService {

    private SpaceshipRepository spaceshipRepository;
    private SpaceshipSensorDataRepository spaceshipSensorDataRepository;
    private AlertRepository alertRepository;


    @PostConstruct
    public void init() {
        createDefaultSpaceship();
    }
    
    public void createDefaultSpaceship(){
        if (spaceshipRepository.count() == 0) {
            Spaceship ship = new Spaceship();
            ship.setName("Spaceship 1");
            ship.setStatus("Active");
            spaceshipRepository.save(ship);
        }
    }

    public List<Spaceship> getAllSpaceships() {
        return spaceshipRepository.findAll();
    }

    public Spaceship getSpaceshipById(Long id) {
        Optional<Spaceship> spaceship = spaceshipRepository.findById(id);
        return spaceship.get();
    }

    public Spaceship createSpaceship(Spaceship spaceship) {
        return spaceshipRepository.save(spaceship);
    }

    public Spaceship updateSpaceship(Spaceship spaceship) {
        return spaceshipRepository.save(spaceship);
    }

    public void deleteSpaceship(Long id) {
        spaceshipRepository.deleteById(id);
    }

    public void deleteAllSpaceships() {
        spaceshipRepository.deleteAll();
    }

    public List<SpaceshipSensorData> getAllSpaceshipSensorData() {
        return spaceshipSensorDataRepository.findAll();
    }

    public SpaceshipSensorData getSpaceshipSensorDataById(Long id) {
        Optional<SpaceshipSensorData> spaceshipSensorData = spaceshipSensorDataRepository.findById(id);
        return spaceshipSensorData.get();
    }

    public SpaceshipSensorData createSpaceshipSensorData(Long spaceshipId, SpaceshipSensorData spaceshipSensorData) {
        Spaceship spaceship = getSpaceshipById(spaceshipId);
        spaceshipSensorData.setSpaceship(spaceship);
        return spaceshipSensorDataRepository.save(spaceshipSensorData);
    }

    public SpaceshipSensorData createSensorDataByMessage(Long spaceshipId, List<Double> values, LocalDateTime timestamp) {
        Spaceship spaceship = getSpaceshipById(spaceshipId);
        SpaceshipSensorData sensorData = new SpaceshipSensorData();
        sensorData.setSpaceship(spaceship);
        sensorData.setCabinTemperature(values.get(0));
        sensorData.setCabinPressure(values.get(1));
        sensorData.setCo2Level(values.get(2));
        sensorData.setPpo2Level(values.get(3));
        sensorData.setIntTemperature(values.get(4));
        sensorData.setExtTemperature(values.get(5));
        sensorData.setHumidity(values.get(6));
        sensorData.setBattery(values.get(7));
        sensorData.setVelocity(values.get(8));
        sensorData.setAltitude(values.get(9));
        sensorData.setApogee(values.get(10));
        sensorData.setPerigee(values.get(11));
        sensorData.setInclination(values.get(12));
        sensorData.setRange(values.get(13));
        sensorData.setTimestamp(timestamp);
        return spaceshipSensorDataRepository.save(sensorData);
    }

    public void deleteAllSpaceshipSensorData() {
        spaceshipSensorDataRepository.deleteAll();
    }

    public List<SpaceshipSensorData> getSpaceshipSensorDataByTimestamp(Long id, String TIMESTAMP) {
        Spaceship spaceship = getSpaceshipById(id);
        List<SpaceshipSensorData> sensorData = spaceship.getSensorData();

        LocalDateTime timestamp = LocalDateTime.parse(TIMESTAMP);
        List<SpaceshipSensorData> result = new ArrayList<>();
        for (SpaceshipSensorData data : sensorData) {
            if (data.getTimestamp().equals(timestamp)) {
                result.add(data);
            }
        }

        return result;
    }

    public List<SpaceshipSensorData> getSpaceshipSensorDataBySpaceshipId(Long id) {
        Spaceship spaceship = getSpaceshipById(id);
        return spaceship.getSensorData();
    }

    public List<SpaceshipSensorData> getSpaceshipSensorDataByTimestamp(String TIMESTAMP) {
        List<SpaceshipSensorData> sensorData = getAllSpaceshipSensorData();
        LocalDateTime timestamp = LocalDateTime.parse(TIMESTAMP);
        System.out.println(timestamp);
        List<SpaceshipSensorData> result = new ArrayList<>();
        for (SpaceshipSensorData data : sensorData) {
            if (data.getTimestamp().equals(timestamp)) {
                result.add(data);
            }
        }

        return null;
    }

    public Spaceship updateSpaceshipStatus(Long id, String newStatus) {
        Optional<Spaceship> spaceshipOptional = spaceshipRepository.findById(id);
        if (spaceshipOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spaceship not found");
        }
        Spaceship spaceship = spaceshipOptional.get();
        spaceship.setStatus(newStatus);
        return spaceshipRepository.save(spaceship);
    }

    public SpaceshipSensorData getLatestSpaceshipSensorData(Long id) {
        Spaceship spaceship = getSpaceshipById(id);
        List<SpaceshipSensorData> sensorData = spaceship.getSensorData();
        if (sensorData.isEmpty()) {
            return null;
        }
        return sensorData.get(sensorData.size() - 1);
    }
    
    public List<Alert> checkSpaceshipStatus(Long spaceshipId, SpaceshipSensorData sensorData) {
        List<Alert> alerts = new ArrayList<>();
    
        // Define normal ranges for each sensor
        final Map<String, Map<String, Double>> NORMAL_RANGES = new HashMap<>();
        NORMAL_RANGES.put("cabinTemperature", Map.of("min", 17.0, "max", 28.0));
        NORMAL_RANGES.put("cabinPressure", Map.of("min", 0.95, "max", 1.50));
        NORMAL_RANGES.put("co2Level", Map.of("min", 320.0, "max", 480.0));
        NORMAL_RANGES.put("ppo2Level", Map.of("min", 20.0, "max", 23.0));
        NORMAL_RANGES.put("intTemperature", Map.of("min", 12.0, "max", 30.0));
        NORMAL_RANGES.put("extTemperature", Map.of("min", -90.0, "max", 40.0));
        NORMAL_RANGES.put("humidity", Map.of("min", 25.0, "max", 75.0));
        NORMAL_RANGES.put("battery", Map.of("min", 30.0, "max", 95.0));
        NORMAL_RANGES.put("velocity", Map.of("min", 2000.0, "max", 7000.0));
        NORMAL_RANGES.put("altitude", Map.of("min", 500.0, "max", 30000.0));
        NORMAL_RANGES.put("apogee", Map.of("min", 400.0, "max", 300000.0));
        NORMAL_RANGES.put("perigee", Map.of("min", 250.0, "max", 250000.0));
        NORMAL_RANGES.put("inclination", Map.of("min", 10.0, "max", 130.0));
        NORMAL_RANGES.put("range", Map.of("min", 150.0, "max", 2000.0));
    
        // Map each sensor name to the corresponding getter and ShipSystem
        Map<String, ShipSystem> sensorToSystemMap = new HashMap<>();
        sensorToSystemMap.put("cabinTemperature", ShipSystem.THERMAL_SYSTEM);
        sensorToSystemMap.put("cabinPressure", ShipSystem.VENTILATION);
        sensorToSystemMap.put("co2Level", ShipSystem.VENTILATION);
        sensorToSystemMap.put("ppo2Level", ShipSystem.VENTILATION);
        sensorToSystemMap.put("intTemperature", ShipSystem.THERMAL_SYSTEM);
        sensorToSystemMap.put("extTemperature", ShipSystem.THERMAL_SYSTEM);
        sensorToSystemMap.put("humidity", ShipSystem.VENTILATION);
        sensorToSystemMap.put("battery", ShipSystem.POWER_SYSTEM);
        sensorToSystemMap.put("velocity", ShipSystem.SURROUNDINGS_AND_NAVIGATION);
        sensorToSystemMap.put("altitude", ShipSystem.SURROUNDINGS_AND_NAVIGATION);
        sensorToSystemMap.put("apogee", ShipSystem.SURROUNDINGS_AND_NAVIGATION);
        sensorToSystemMap.put("perigee", ShipSystem.SURROUNDINGS_AND_NAVIGATION);
        sensorToSystemMap.put("inclination", ShipSystem.SURROUNDINGS_AND_NAVIGATION);
        sensorToSystemMap.put("range", ShipSystem.SURROUNDINGS_AND_NAVIGATION);
    
        // Iterate over each sensor and check its value against normal ranges
        for (Map.Entry<String, Map<String, Double>> entry : NORMAL_RANGES.entrySet()) {
            String sensor = entry.getKey();
            Map<String, Double> range = entry.getValue();
    
            // Retrieve the value of the sensor dynamically
            double value;
            try {
                value = (double) SpaceshipSensorData.class
                        .getMethod("get" + Character.toUpperCase(sensor.charAt(0)) + sensor.substring(1))
                        .invoke(sensorData);
            } catch (Exception e) {
                throw new RuntimeException("Error reading sensor value: " + sensor, e);
            }
    
            // Get the associated ship system
            ShipSystem shipSystem = sensorToSystemMap.get(sensor);
    
            // Create alerts if the value is out of range
            Alert a =  checkAndCreateAlert(spaceshipId, shipSystem, sensor, value, range);
            if (a != null) {
                alerts.add(a);
            }
        }
    
        return alerts;
    }
    

    // // Helper function to check if a value is outside the allowed range and create an alert
    private Alert checkAndCreateAlert(Long spaceshipId, ShipSystem shipSystem, String sensor ,double value, Map<String, Double> range) {
        double min = range.get("min");
        double max = range.get("max");

        //check the priority by how far the value is from the normal range, using the percentage of the range
        double percentage = (value - min) / (max - min);
        Priority priority;
        if (percentage < 0.25) {
            priority = Priority.LOW;
        } else if (percentage < 0.75) {
            priority = Priority.MEDIUM;
        } else {
            priority = Priority.HIGH;
        }
        
        // If the value is outside the range, create an alert
        Alert alert = null;
        if (value < min) {
            alert = createAlert(spaceshipId, priority, "TO BE FIXED", shipSystem, sensor + " is below the normal range, should be above " + min);
        } else if (value > max) {
            alert = createAlert(spaceshipId, priority, "TO BE FIXED", shipSystem, sensor + " is above the normal range, should be below " + max);
        }
        return alert;

    }

    // Helper function to create an alert
    private Alert createAlert(Long spaceshipId, Priority priority, String status, ShipSystem shipSystem, String cause) {
        Alert alert = new Alert();
        alert.setSpaceship(getSpaceshipById(spaceshipId));
        alert.setPriority(priority);
        alert.setStatus(status);
        alert.setShipSystem(shipSystem);
        alert.setCause(cause);
        alert.setTimestamp(LocalDateTime.now());
        alert.setResolved(false);
        cleanupOldAlerts();
        alertRepository.save(alert);  // Save the alert to the repository
        return alert;
    }

    // public Alert createAlert(Long spaceshipId, Alert alert) {
    //     Spaceship spaceship = getSpaceshipById(spaceshipId);
    //     alert.setSpaceship(spaceship);
    //     return alertRepository.save(alert);
    // }
        
    //stabilize the value of the sensor data and emit an alert that the value is stable
    // private Alert stabilizeValue(Long spaceshipId, ShipSystem shipSystem, String sensor, double value, Map<String, Double> range) {
    //     Priority priority = Priority.FIXED;
    //     Alert alert = null;
    //     //set the value to a random value within the normal range
    //     double min = range.get("min");
    //     double max = range.get("max");
    //     double stabilizedValue = Math.random() * (max - min) + min;

    //     //create an alert that the value is stable
    //     alert = createAlert(spaceshipId, priority, "FIXED", shipSystem, sensor + " is now stable at " + stabilizedValue);
    //     return alert;
    // }

    public List<Alert> getAlerts(Long id) {
        Spaceship spaceship = getSpaceshipById(id);
        return spaceship.getAlerts();
    }

    public List<SpaceshipSensorData> getSpaceshipSensorDataByTimeFrame(Long id, LocalDateTime start,
            LocalDateTime end) {

        // FIrst get all the sensor data from the spaceship
        List<SpaceshipSensorData> sensorData = getSpaceshipSensorDataBySpaceshipId(id);

        // Then filter the data by the time frame
        return sensorData.stream()
                .filter(data -> !data.getTimestamp().isBefore(start) && !data.getTimestamp().isAfter(end))
                .collect(Collectors.toList());

    }

    @Transactional
    public void cleanupOldSensorData() {
        int MAX_RECORDS = 5000;
        int BATCH_SIZE = 500;

        long totalRecords = spaceshipSensorDataRepository.count();
        
        System.out.println("total sensor data = " + totalRecords); 

        if (totalRecords > MAX_RECORDS) {
            Pageable pageable = PageRequest.of(0, BATCH_SIZE); 
            List<Long> oldestIds = spaceshipSensorDataRepository.findOldestIdsToRemove(pageable);
            System.out.println("ids to remove: ");
            System.out.println(oldestIds);

            if (!oldestIds.isEmpty()) {
                System.out.println("vai entrar no delete geral");
                spaceshipSensorDataRepository.deleteAllByIdInBatch(oldestIds);
                System.out.println("passou pelo delete geral");
            }
        }
    }

    @Transactional
    public void cleanupOldAlerts() {
        int MAX_RECORDS = 200;
        int BATCH_SIZE = 50;

        long totalRecords = alertRepository.count(); 

        if (totalRecords > MAX_RECORDS) {
            Pageable pageable = PageRequest.of(0, BATCH_SIZE); 
            List<Long> oldestIds = alertRepository.findOldestIdsToRemove(pageable);

            if (!oldestIds.isEmpty()) {
                alertRepository.deleteAllByIdInBatch(oldestIds);
            }
        }
    }



}
