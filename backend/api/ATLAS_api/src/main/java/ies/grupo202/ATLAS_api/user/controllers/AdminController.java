package ies.grupo202.ATLAS_api.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.grupo202.ATLAS_api.user.dtos.RegisterUserDto;
import ies.grupo202.ATLAS_api.user.entities.User;
import ies.grupo202.ATLAS_api.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequestMapping("/api/v1/admins")
@RestController
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new CEO")
    @ApiResponse(responseCode = "200", description = "CEO created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("/ceo")
    // @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<User> createCEO(@RequestBody RegisterUserDto registerUserDto) {
        User createdDirector = userService.createCEO(registerUserDto);
        return ResponseEntity.ok(createdDirector);
    }

    @Operation(summary = "Create a new Flight Director")
    @ApiResponse(responseCode = "200", description = "Flight Director created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("/flightdirector")
    // @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<User> createFlightDirector(@RequestBody RegisterUserDto registerUserDto) {
        User createdFlightDirector = userService.createFlightDirector(registerUserDto);
        return ResponseEntity.ok(createdFlightDirector);
    }
}
