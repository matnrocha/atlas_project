package ies.grupo202.ATLAS_api.spaceship.entities;

import ies.grupo202.ATLAS_api.spaceship.entities.Enums.Priority;
import ies.grupo202.ATLAS_api.spaceship.entities.Enums.ShipSystem;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spaceship_alerts")

public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Not sure if lazy is correct
    @JoinColumn(name = "spaceship_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Spaceship spaceship;

    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private ShipSystem shipSystem;

    @Column(nullable = false)
    private String cause; // A simple message with perhaps data

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean resolved;

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", spaceship=" + spaceship +
                ", priority=" + priority +
                ", status='" + status + '\'' +
                ", shipSystem=" + shipSystem +
                ", cause='" + cause + '\'' +
                ", timestamp=" + timestamp +
                ", resolved=" + resolved +
                '}';
    }
}
