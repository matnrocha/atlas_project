package ies.grupo202.ATLAS_api.astronaut.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.grupo202.ATLAS_api.astronaut.entities.Astronaut;
import ies.grupo202.ATLAS_api.astronaut.entities.VitalSigns;
import ies.grupo202.ATLAS_api.astronaut.services.AstronautService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
 

@RestController
@RequestMapping("/api/v1/astronaut")
public class AstronautController {
    private AstronautService astronautService;

    @Autowired
    public AstronautController(AstronautService astronautService) {
        this.astronautService = astronautService;
    }

    //-------------------Astronaut Endpoints-------------------
    
    @Operation(summary = "Get an astronaut by id")
    @ApiResponse(responseCode = "200", description = "Astronaut found")
    @ApiResponse(responseCode = "404", description = "Astronaut not found")
    @GetMapping("/astronaut/{id}")
    public ResponseEntity<Astronaut> getAstronautById(Long id) {
        Astronaut astronaut = astronautService.getAstronautById(id);
        return astronaut != null ? ResponseEntity.ok(astronaut) : ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Get all astronauts")
    @ApiResponse(responseCode = "200", description = "Astronauts found")
    @ApiResponse(responseCode = "404", description = "Astronauts not found")
    @GetMapping("/astronauts")
    public ResponseEntity<List<Astronaut>> getAllAstronauts() {
        List<Astronaut> astronauts = astronautService.getAllAstronauts();
        return astronauts != null ? ResponseEntity.ok(astronauts) : ResponseEntity.badRequest().build();
    }
    
    @Operation(summary = "Create a new astronaut")
    @ApiResponse(responseCode = "201", description = "Astronaut created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("/astronaut")
    public ResponseEntity<Astronaut> createAstronaut(@RequestBody Astronaut astronaut) {
        Astronaut newAstronaut = astronautService.createAstronaut(astronaut);
        return newAstronaut != null ? ResponseEntity.ok(newAstronaut) : ResponseEntity.badRequest().build();
    }


    //-------------------VitalSigns Endpoints-------------------

    @Operation(summary = "Get vital signs by id")
    @ApiResponse(responseCode = "200", description = "Vital signs found")
    @ApiResponse(responseCode = "404", description = "Vital signs not found")
    @GetMapping("/vitalSigns/{id}")
    public ResponseEntity<List<VitalSigns>> getVitalSignsById(@PathVariable Long id) {
        List<VitalSigns> vitalSigns = astronautService.getVitalSignsById(id);
        return vitalSigns.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(vitalSigns);
    }

    @Operation(summary = "Create new vital signs")
    @ApiResponse(responseCode = "201", description = "Vital signs created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("/vitalSigns/{id}")
    public ResponseEntity<VitalSigns> addVitalSigns(@PathVariable Long id, @RequestBody VitalSigns vitalSigns) {
        VitalSigns saved_vitalSigns = astronautService.createVitalSigns(id,vitalSigns);
        return new ResponseEntity<>(saved_vitalSigns, HttpStatus.CREATED);
    }

    @DeleteMapping("/vitalSigns/cleanup")
    public ResponseEntity<String> cleanupOldVitalSigns() {
        astronautService.cleanupOldVitalSigns();
        return ResponseEntity.ok().build();
    }

}
