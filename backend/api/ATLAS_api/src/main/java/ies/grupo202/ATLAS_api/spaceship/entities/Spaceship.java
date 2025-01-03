package ies.grupo202.ATLAS_api.spaceship.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spaceships")
public class Spaceship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "spaceship") // , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpaceshipSensorData> sensorData;

    @OneToMany(mappedBy = "spaceship") // , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alerts;

    @OneToMany(mappedBy = "spaceship") // , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageLog> messageLogs;

}