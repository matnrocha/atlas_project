package ies.grupo202.ATLAS_api.astronaut.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ies.grupo202.ATLAS_api.astronaut.entities.VitalSigns;

public interface VitalSignsRepository extends JpaRepository<VitalSigns, Long> {   
    @Query("SELECT vs FROM VitalSigns vs WHERE vs.astronaut.id = :id")
    VitalSigns getVitalSignsById(@Param("id") Long id);

    @Query("SELECT v.id FROM VitalSigns v ORDER BY v.lastUpdate ASC")
    List<Long> findOldestIdsToRemove(Pageable pageable);

}
