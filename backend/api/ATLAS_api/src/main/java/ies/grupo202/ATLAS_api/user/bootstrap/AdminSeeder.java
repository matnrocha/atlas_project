package ies.grupo202.ATLAS_api.user.bootstrap;

import ies.grupo202.ATLAS_api.user.dtos.RegisterUserDto;
import ies.grupo202.ATLAS_api.user.entities.Role;
import ies.grupo202.ATLAS_api.user.entities.RoleEnum;
import ies.grupo202.ATLAS_api.user.entities.User;
import ies.grupo202.ATLAS_api.user.repositories.RoleRepository;
import ies.grupo202.ATLAS_api.user.repositories.UserRepository;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@DependsOn("roleSeeder")
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(
        RoleRepository roleRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
        this.createAstronauts();
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setFullName("Melon Usk").setEmail("super.admin@email.com").setPassword("123456");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CEO);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty()) {
            System.out.println("Role CEO not found.");
            return;
        }

        if (optionalUser.isPresent()) {
            System.out.println("User with this email already exists.");
            return;
        }

        var user = new User()
            .setFullName(userDto.getFullName())
            .setEmail(userDto.getEmail())
            .setPassword(passwordEncoder.encode(userDto.getPassword()))
            .setRole(optionalRole.get());

        userRepository.save(user);
    }

    private void createAstronauts() {
        String[][] astronauts = {
            {"Saylor Twift", "saylor.twift@email.com", "saylor.twift"},
            {"Sus Ana", "sus.ana@email.com", "sus.ana"},
            {"Juan Direction", "juan.direction@email.com", "juan.direction"},
            {"Lua Dipa", "lua.dipa@email.com", "lua.dipa"},
            {"João Cuco", "joao.cuco@email.com", "joao.cuco"}
        };
    
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ASTRONAUT);
    
        if (optionalRole.isEmpty()) {
            System.out.println("Role ASTRONAUT not found.");
            return;
        }
    
        for (String[] astronautData : astronauts) {
            String fullName = astronautData[0];
            String email = astronautData[1];
            String password = astronautData[2];
    
            Optional<User> optionalUser = userRepository.findByEmail(email);
    
            if (optionalUser.isPresent()) {
                System.out.println("User with email " + email + " already exists.");
                continue;
            }
    
            RegisterUserDto userDto = new RegisterUserDto()
                .setFullName(fullName)
                .setEmail(email)
                .setPassword(password);
    
            var user = new User()
                .setFullName(userDto.getFullName())
                .setEmail(userDto.getEmail())
                .setPassword(passwordEncoder.encode(userDto.getPassword()))
                .setRole(optionalRole.get());
    
            userRepository.save(user);
            System.out.println("Created astronaut: " + fullName);
        }
    }    
}
