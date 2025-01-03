package ies.grupo202.ATLAS_api.user.services;

import ies.grupo202.ATLAS_api.user.dtos.LoginUserDto;
import ies.grupo202.ATLAS_api.user.dtos.RegisterUserDto;
import ies.grupo202.ATLAS_api.user.entities.Role;
import ies.grupo202.ATLAS_api.user.entities.RoleEnum;
import ies.grupo202.ATLAS_api.user.entities.User;
import ies.grupo202.ATLAS_api.user.repositories.RoleRepository;
import ies.grupo202.ATLAS_api.user.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${cors.allowed-origins}")
    private String stringCors; 

    public AuthenticationService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        String chosenRole = input.getPreferredRole();
        if (chosenRole == null || chosenRole.isBlank()) {
            chosenRole = "CEO";
        }
        
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.valueOf(chosenRole.toUpperCase()));

        if (optionalRole.isEmpty()) {
            return null;
        }

        var user = new User()
            .setFullName(input.getFullName())
            .setEmail(input.getEmail())
            .setPassword(passwordEncoder.encode(input.getPassword()))
            .setRole(optionalRole.get());

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
            )
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public void printCorsValue() {
        List<String> corsOriginsList = Arrays.asList(stringCors.split(","));

        // Imprime cada item da lista
        System.out.println("Lista de cors.allowed-origins:");
        corsOriginsList.forEach(System.out::println);
    }
}