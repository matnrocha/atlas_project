package ies.grupo202.ATLAS_api.spaceship.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spaceship_sensor_data")
public class SpaceshipSensorData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Not sure if lazy is correct
    @JoinColumn(name = "spaceship_id",referencedColumnName = "id" ,nullable = false)
    @JsonBackReference
    private Spaceship spaceship;
    
    @Column(nullable = false)
    private double cabinTemperature;
    
    @Column(nullable = false)
    private double cabinPressure;
    
    @Column(nullable = false)
    private double co2Level;

    @Column(nullable = false)
    private double ppo2Level;

    @Column(nullable = false)
    private double intTemperature;

    @Column(nullable = false)
    private double extTemperature;

    @Column(nullable = false)
    private double humidity;

    @Column(nullable = false)
    private double battery;
    
    @Column(nullable = false)
    private double velocity;
    
    @Column(nullable = false)
    private double altitude;

    @Column(nullable = false)
    private double apogee;

    @Column(nullable = false)
    private double perigee;

    @Column(nullable = false)
    private double inclination;

    @Column(nullable = false)
    private double range;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
