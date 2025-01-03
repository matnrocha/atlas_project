package ies.grupo202.ATLAS_api.astronaut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ies.grupo202.ATLAS_api.astronaut.entities.Astronaut;
import ies.grupo202.ATLAS_api.astronaut.entities.VitalSigns;

public interface AstronautRepository extends JpaRepository<Astronaut, Long> {   
    @Query("SELECT vs FROM VitalSigns vs WHERE vs.astronaut.id = :id")
    VitalSigns getVitalSignsById(@Param("id") Long id);
}
