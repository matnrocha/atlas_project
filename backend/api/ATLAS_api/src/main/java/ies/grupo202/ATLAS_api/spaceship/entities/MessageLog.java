package ies.grupo202.ATLAS_api.spaceship.entities;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message_logs")
public class MessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "spaceship_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Spaceship spaceship;

    @OneToMany(mappedBy = "messageLog")
    private List<Message> messages;
}
