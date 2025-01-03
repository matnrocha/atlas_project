package ies.grupo202.ATLAS_api.spaceship.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.grupo202.ATLAS_api.spaceship.entities.Spaceship;
import ies.grupo202.ATLAS_api.spaceship.entities.Alert;
import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipSensorData;
import ies.grupo202.ATLAS_api.spaceship.services.SpaceshipService;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/spaceship")
public class SpaceshipController {

    private SpaceshipService spaceshipService;


    @Operation(summary = "Get all spaceships")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/all")
    public List<Spaceship> getAllSpaceships() {
        return spaceshipService.getAllSpaceships();
    }


    @Operation(summary = "Add a new spaceship")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping("/add")
    public ResponseEntity<Spaceship> addSpaceship(@RequestBody Spaceship spaceship) {
        Spaceship saved_spaceship = spaceshipService.createSpaceship(spaceship);
        return new ResponseEntity<>(saved_spaceship, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the latest sensor data from a spaceship")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("{id}/latest_sensor_data/")
    public ResponseEntity<SpaceshipSensorData> getLatestSensorData(@PathVariable Long id) {
        SpaceshipSensorData sensorData = spaceshipService.getLatestSpaceshipSensorData(id);

        if (sensorData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(sensorData, HttpStatus.OK);
        }
    }


    @Operation(summary = "Add sensor data to a spaceship")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping("{id}/sensor_data/")
    public ResponseEntity<SpaceshipSensorData> addSensorData(@PathVariable Long id, @RequestBody SpaceshipSensorData sensorData) {
        SpaceshipSensorData saved_sensorData = spaceshipService.createSpaceshipSensorData(id,sensorData);
        return new ResponseEntity<>(saved_sensorData, HttpStatus.CREATED);
    }


    @Operation(summary = "Get sensor data from a spaceship by timestamp")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("{id}/sensor_data/{TIMESTAMP}")
    public ResponseEntity<List<SpaceshipSensorData>> getSensorDataById_Timestamp(@PathVariable Long id, @PathVariable String TIMESTAMP) {
        List<SpaceshipSensorData> sensorData = spaceshipService.getSpaceshipSensorDataByTimestamp(id, TIMESTAMP);

        if (sensorData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(sensorData, HttpStatus.OK);
        }
    }


    @Operation(summary = "Get sensor data from a spaceship")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("{id}/sensor_data/")
    public ResponseEntity<List<SpaceshipSensorData>> getSensorDataById(@PathVariable Long id) {
        List<SpaceshipSensorData> sensorData = spaceshipService.getSpaceshipSensorDataBySpaceshipId(id);

        if (sensorData.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(sensorData, HttpStatus.OK);
        }
    }


    @Operation(summary = "Get sensor data by timestamp (from all spaceships)")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/sensor_data/{TIMESTAMP}")
    public ResponseEntity<List<SpaceshipSensorData>> getSensorDataByTimestamp(@PathVariable String TIMESTAMP) {
        List<SpaceshipSensorData> sensorData = spaceshipService.getSpaceshipSensorDataByTimestamp(TIMESTAMP);
        if (sensorData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(sensorData, HttpStatus.OK);
        }
    }

    @DeleteMapping("/sensor_data/cleanup")
    public ResponseEntity<String> cleanupOldVitalSigns() {
        spaceshipService.cleanupOldSensorData();
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete a spaceship")
    @ApiResponse(responseCode = "404", description = "Not found")
    @DeleteMapping("{id}/delete/")
    public ResponseEntity<?> deleteSpaceship(@PathVariable Long id) {
        spaceshipService.deleteSpaceship(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Update the spaceship status")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping("{id}/update_status/")
    public ResponseEntity<Spaceship> updateSpaceshipStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> statusUpdate) {
        String newStatus = statusUpdate.get("status");
        Spaceship updatedSpaceship = spaceshipService.updateSpaceshipStatus(id, newStatus);
        return new ResponseEntity<>(updatedSpaceship, HttpStatus.OK);
    }


    // @Operation(summary = "Post a new alert to a spaceship")
    // @ApiResponse(responseCode = "404", description = "Not found")
    // @PostMapping("{id}/alerts/")
    // public ResponseEntity<Alert> addAlert(@PathVariable Long id, @RequestBody Alert alert) {
    //     Alert saved_alert = spaceshipService.createAlert(id, alert);
    //     return new ResponseEntity<>(saved_alert, HttpStatus.OK);

    // }

    @Operation(summary = "Get all the alerts from a spaceship")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("{id}/alerts/")
    public ResponseEntity<List<Alert>> getAlerts(@PathVariable Long id) {
        // spaceshipService.checkSpaceshipStatus(id);
        List<Alert> alerts = spaceshipService.getAlerts(id);

        if (alerts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(alerts, HttpStatus.OK);
        }
    }



@GetMapping("{id}/report/{TIMESTAMP0}/{TIMESTAMP1}")
public ResponseEntity<List<SpaceshipSensorData>> getSensorDataByTimeFrame(
        @PathVariable Long id,
        @PathVariable String TIMESTAMP0,
        @PathVariable String TIMESTAMP1) {
    try {
        LocalDateTime start = LocalDateTime.parse(TIMESTAMP0);
        LocalDateTime end = LocalDateTime.parse(TIMESTAMP1);

        if (start.isAfter(end)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }

        List<SpaceshipSensorData> sensorData = spaceshipService.getSpaceshipSensorDataByTimeFrame(id, start, end);

        if (sensorData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(sensorData, HttpStatus.OK);
    } catch (DateTimeParseException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }
}


}

