package ies.grupo202.ATLAS_api.astronaut.entities;

import ies.grupo202.ATLAS_api.astronaut.entities.Enums.Role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "astronauts")
public class Astronaut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "astronaut")
    // @JoinColumn(name = "vital_signs_id")
    private List<VitalSigns> vitalSigns;

    @Enumerated(EnumType.STRING)
    private Role role;
}
