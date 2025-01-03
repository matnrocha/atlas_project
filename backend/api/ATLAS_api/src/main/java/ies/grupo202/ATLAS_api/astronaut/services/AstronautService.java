package ies.grupo202.ATLAS_api.astronaut.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ies.grupo202.ATLAS_api.astronaut.entities.Astronaut;
import ies.grupo202.ATLAS_api.astronaut.entities.Enums.Role;
import ies.grupo202.ATLAS_api.astronaut.entities.VitalSigns;
import ies.grupo202.ATLAS_api.astronaut.repositories.AstronautRepository;
import ies.grupo202.ATLAS_api.astronaut.repositories.VitalSignsRepository;
import ies.grupo202.ATLAS_api.spaceship.entities.Alert;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.Priority;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.ShipSystem;
import ies.grupo202.ATLAS_api.spaceship.entities.Spaceship;
import ies.grupo202.ATLAS_api.spaceship.repositories.AlertRepository;
import ies.grupo202.ATLAS_api.spaceship.repositories.SpaceshipRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AstronautService {

    private AstronautRepository astronautRepository;
    private VitalSignsRepository vitalSignsRepository;
    private AlertRepository alertRepository;
    private SpaceshipRepository spaceshipRepository;

    @PostConstruct
    public void init() {
        createDefaultAstronauts();
    }

    public void createDefaultAstronauts() {
        if (astronautRepository.count() == 0) {
            Astronaut astronaut = new Astronaut();
            astronaut.setName("Saylor Twift");
            astronaut.setRole(Role.MECHANIC);
            astronautRepository.save(astronaut);

            Astronaut astronaut2 = new Astronaut();
            astronaut2.setName("Sus Ana");
            astronaut2.setRole(Role.PILOT);
            astronautRepository.save(astronaut2);

            Astronaut astronaut3 = new Astronaut();
            astronaut3.setName("Juan Direction");
            astronaut3.setRole(Role.DOCTOR);
            astronautRepository.save(astronaut3);

            Astronaut astronaut4 = new Astronaut();
            astronaut4.setName("Lua Dipa");
            astronaut4.setRole(Role.ENGINEER);
            astronautRepository.save(astronaut4);

            Astronaut astronaut5 = new Astronaut();
            astronaut5.setName("João Cuco");
            astronaut5.setRole(Role.SCIENTIST);
            astronautRepository.save(astronaut5);
        }
    }

    public Astronaut getAstronautById(Long id) {
        return astronautRepository.findById(id).get();
    }

    public List<Astronaut> getAllAstronauts() {
        return astronautRepository.findAll();
    }

    public Astronaut createAstronaut(Astronaut astronaut) {
        return astronautRepository.save(astronaut);
    }

    public Astronaut updateSpaceship(Astronaut astronaut) {
        return astronautRepository.save(astronaut);
    }

    public void deleteAstronaut(Long id) {
        astronautRepository.deleteById(id);
    }

    public List<VitalSigns> getVitalSignsById(Long id) {
        Long astronautId = astronautRepository.findById(id).get().getId();
        List<VitalSigns> vitalSigns = new ArrayList<>();
        for (VitalSigns v : vitalSignsRepository.findAll()) {
            if (v.getAstronaut().getId() == astronautId) {
                vitalSigns.add(v);
            }
        }
        return vitalSigns;
    }

    public VitalSigns createVitalSigns(Long id, VitalSigns vitalSigns) {
        Astronaut astronaut = astronautRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Astronaut not found"));

        vitalSigns.setAstronaut(astronaut);
        return vitalSignsRepository.save(vitalSigns);
    }

    public List<VitalSigns> updateVitalSigns(Long id, List<VitalSigns> vitalSigns) {
        Astronaut astronaut = getAstronautById(id);
        if (astronaut == null) {
            return null;
        }
        astronaut.setVitalSigns(vitalSigns);
        astronautRepository.save(astronaut);
        return vitalSigns;
    }

    @Transactional
    public void cleanupOldVitalSigns() {
        int MAX_RECORDS = 5000;
        int BATCH_SIZE = 500;

        long totalRecords = vitalSignsRepository.count();

        if (totalRecords > MAX_RECORDS) {
            Pageable pageable = PageRequest.of(0, BATCH_SIZE);
            List<Long> oldestIds = vitalSignsRepository.findOldestIdsToRemove(pageable);

            if (!oldestIds.isEmpty()) {
                vitalSignsRepository.deleteAllByIdInBatch(oldestIds);
            }
        }
    }

    public VitalSigns createVitalSignsByMessage(Long astronautId, List<Double> values, LocalDateTime timestamp) {
        Astronaut astronaut = getAstronautById(astronautId);
        VitalSigns vitalSigns = new VitalSigns();
        vitalSigns.setAstronaut(astronaut);
        vitalSigns.setHeartRate(values.get(0));
        vitalSigns.setBloodPressure(values.get(1));
        vitalSigns.setBodyTemperature(values.get(2));
        vitalSigns.setOxygenLevel(values.get(3));
        vitalSigns.setLastUpdate(timestamp);
        return vitalSignsRepository.save(vitalSigns);
    }

    public List<Alert> checkAstronautStatus(Long astronautId, VitalSigns vitalSigns) {
        List<Alert> alerts = new ArrayList<>();

        final Map<String, Map<String, Double>> NORMAL_RANGES = new HashMap<>();
        NORMAL_RANGES.put("heartRate", Map.of("min", 65.0, "max", 105.0));
        NORMAL_RANGES.put("bloodPressure", Map.of("min", 95.0, "max", 100.0));
        NORMAL_RANGES.put("oxygenLevel", Map.of("min", 92.0, "max", 100.0));
        NORMAL_RANGES.put("bodyTemperature", Map.of("min", 36.5, "max", 38.0));

        Map<String, ShipSystem> vitalsToSystemMap = new HashMap<>();
        vitalsToSystemMap.put("heartRate", ShipSystem.LIFE_SUPPORT);
        vitalsToSystemMap.put("bloodPressure", ShipSystem.LIFE_SUPPORT);
        vitalsToSystemMap.put("oxygenLevel", ShipSystem.LIFE_SUPPORT);
        vitalsToSystemMap.put("bodyTemperature", ShipSystem.LIFE_SUPPORT);

        for (Map.Entry<String, Map<String, Double>> entry : NORMAL_RANGES.entrySet()) {
            String vital = entry.getKey();
            Map<String, Double> range = entry.getValue();

            // Retrieve the value of the vital dynamically
            double value;
            try {
                value = (double) vitalSigns.getClass()
                        .getMethod("get" + vital.substring(0, 1).toUpperCase() + vital.substring(1)).invoke(vitalSigns);
            } catch (Exception e) {
                throw new RuntimeException("Error reading vital value: " + vital, e);
            }

            ShipSystem system = vitalsToSystemMap.get(vital);

            // Create alerts if the value is out of range
            Alert a = checkAndCreateAlert(system, vital, value, range, astronautId);
            if (a != null) {
                alerts.add(a);
            }
        }
        return alerts;
    };

    private Alert checkAndCreateAlert(ShipSystem shipSystem, String vital, double value, Map<String, Double> range,
            Long astronautId) {
        double min = range.get("min");
        double max = range.get("max");

        String astronautName = astronautRepository.findById(astronautId).get().getName();

        // check the priority by how far the value is from the normal range, using the
        // percentage of the range
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
            alert = createAlert(priority, "TO BE FIXED", shipSystem,
                    vital + " for " + astronautName + " is below the normal range, should be above " + min);
        } else if (value > max) {
            alert = createAlert(priority, "TO BE FIXED", shipSystem,
                    vital + " for " + astronautName + " is above the normal range, should be below " + max);
        }
        return alert;

    }

    private Alert createAlert(Priority priority, String status, ShipSystem shipSystem, String cause) {
        Alert alert = new Alert();
        Spaceship spaceship = spaceshipRepository.findById((long) 1).get();
        alert.setSpaceship(spaceship);
        alert.setPriority(priority);
        alert.setStatus(status);
        alert.setShipSystem(shipSystem);
        alert.setCause(cause);
        alert.setTimestamp(LocalDateTime.now());
        alert.setResolved(false);
        alertRepository.save(alert); // Save the alert to the repository
        return alert;
    }

}
