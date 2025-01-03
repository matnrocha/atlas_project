package ies.grupo202.ATLAS_api.user.repositories;

import ies.grupo202.ATLAS_api.user.entities.Role;
import ies.grupo202.ATLAS_api.user.entities.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
