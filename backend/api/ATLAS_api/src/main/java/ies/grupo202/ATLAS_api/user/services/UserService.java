package ies.grupo202.ATLAS_api.user.services;

import ies.grupo202.ATLAS_api.user.dtos.RegisterUserDto;
import ies.grupo202.ATLAS_api.user.entities.Role;
import ies.grupo202.ATLAS_api.user.entities.RoleEnum;
import ies.grupo202.ATLAS_api.user.entities.User;
import ies.grupo202.ATLAS_api.user.repositories.RoleRepository;
import ies.grupo202.ATLAS_api.user.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public User createFlightDirector(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.FLIGHT_DIRECTOR);

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

    public User createCEO(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CEO);

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
}