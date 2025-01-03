package ies.grupo202.ATLAS_api.spaceship.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spaceship_reports")
public class SpaceshipReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "spaceship_id",referencedColumnName = "id" ,nullable = false)
    private Spaceship spaceship;

    @Column(nullable = false)
    private String type;

    @OneToMany // Do we need to cascade the effects?
    @JoinColumn(name = "spaceship_report_id")
    private List<SpaceshipSensorData> sensorData;

    @OneToMany // Do we need to cascade the effects?
    @JoinColumn(name = "report_id")
    private List<Alert> alerts; 

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
