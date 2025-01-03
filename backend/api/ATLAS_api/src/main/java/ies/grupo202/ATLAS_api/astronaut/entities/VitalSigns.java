package ies.grupo202.ATLAS_api.astronaut.entities;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vital_signs")
@Entity
public class VitalSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //is this necessary? can the foreign key be used instead?

    @ManyToOne(optional = false, fetch = FetchType.EAGER) // Not sure if lazy is correct
    @JoinColumn(name = "astronaut_id",referencedColumnName = "id" ,nullable = false)
    @JsonBackReference
    private Astronaut astronaut;

    @Column(nullable = false)
    private Double heartRate;

    @Column(nullable = false)
    private Double bloodPressure;

    @Column(nullable = false)
    private Double bodyTemperature;

    @Column(nullable = false)
    private Double oxygenLevel;

    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    public void setLastUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

}
