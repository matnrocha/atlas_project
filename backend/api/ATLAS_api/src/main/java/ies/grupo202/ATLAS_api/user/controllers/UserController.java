package ies.grupo202.ATLAS_api.user.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.grupo202.ATLAS_api.user.dtos.UserDetailsResponse;
import ies.grupo202.ATLAS_api.user.entities.User;
import ies.grupo202.ATLAS_api.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved authenticated user")
    @ApiResponse(responseCode = "401", description = "Unauthorized, user is not authenticated")
    @GetMapping("/me")
    public ResponseEntity<?> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        try {
            User currentUser = (User) authentication.getPrincipal();
            return ResponseEntity.ok(currentUser);
        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user details");
        }
    }


    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all users")
    @ApiResponse(responseCode = "403", description = "Forbidden, not authorized to view users")
    @GetMapping
    //@PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'ENGINEER')")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get details of the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved authenticated user's details")
    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> authenticatedUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        String roleName = currentUser.getRole() != null ? currentUser.getRole().getName().toString() : "No Role Assigned";

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(
                currentUser.getFullName(),
                currentUser.getEmail(),
                roleName
        );

        return ResponseEntity.ok(userDetailsResponse);
    }
}