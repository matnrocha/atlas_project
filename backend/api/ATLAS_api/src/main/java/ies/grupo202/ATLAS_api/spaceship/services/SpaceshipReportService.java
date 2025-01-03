package ies.grupo202.ATLAS_api.spaceship.services;

import lombok.AllArgsConstructor;
//import ies.grupo202.ATLAS_api.spaceship.entities.Spaceship;
import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipReport;
//import ies.grupo202.ATLAS_api.spaceship.entities.SpaceshipSensorData;
import ies.grupo202.ATLAS_api.spaceship.repositories.SpaceshipReportRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SpaceshipReportService {

    private SpaceshipReportRepository spaceshipReportRepository;

    public List<SpaceshipReport> getAllSpaceshipReports() {
        return spaceshipReportRepository.findAll();
    }

    public SpaceshipReport getSpaceshipReportById(Long id) {
        Optional<SpaceshipReport> spaceshipReport = spaceshipReportRepository.findById(id);
        return spaceshipReport.get();
    }

    public SpaceshipReport createSpaceshipReport(SpaceshipReport spaceshipReport) {
        return spaceshipReportRepository.save(spaceshipReport);
    }

    public SpaceshipReport updateSpaceshipReport(SpaceshipReport spaceshipReport) {
        return spaceshipReportRepository.save(spaceshipReport);
    }

    public void deleteSpaceshipReport(Long id) {
        spaceshipReportRepository.deleteById(id);
    }

    public void deleteAllSpaceshipReports() {
        spaceshipReportRepository.deleteAll();
    }

}
