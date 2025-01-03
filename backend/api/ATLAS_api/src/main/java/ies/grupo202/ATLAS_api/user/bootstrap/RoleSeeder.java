package ies.grupo202.ATLAS_api.user.bootstrap;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import ies.grupo202.ATLAS_api.user.entities.Role;
import ies.grupo202.ATLAS_api.user.entities.RoleEnum;
import ies.grupo202.ATLAS_api.user.repositories.RoleRepository;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;


    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.CEO, RoleEnum.FLIGHT_DIRECTOR, RoleEnum.ASTRONAUT };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
            RoleEnum.CEO, "CEO role",
            RoleEnum.FLIGHT_DIRECTOR, "Flight Director role",
            RoleEnum.ASTRONAUT, "Astronaut role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();

                roleToCreate.setName(roleName)
                        .setDescription(roleDescriptionMap.get(roleName));

                roleRepository.save(roleToCreate);
            });
        });
    }
}