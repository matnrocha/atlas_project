package ies.grupo202.ATLAS_api.spaceship.entities;

import ies.grupo202.ATLAS_api.astronaut.entities.Astronaut;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // If the sender is null, the message is from the spaceship itself
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "astronaut_id", referencedColumnName = "id", nullable = true)
    // @JsonBackReference
    private Astronaut sender;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_log_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private MessageLog messageLog;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // // OVerride getter for getSender, for cases where it is null
    // public Astronaut getSender() {
    // if (sender == null) {
    // return new Astronaut();
    // }
    // return sender;
    // }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", sender=" + sender + ", messageLog=" + messageLog + ", message='" + message
                + '\'' + ", timestamp=" + timestamp + '}';
    }

}
