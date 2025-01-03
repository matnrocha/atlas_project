package ies.grupo202.ATLAS_api.spaceship.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipReport;

public interface SpaceshipReportRepository extends JpaRepository<SpaceshipReport, Long> {
}