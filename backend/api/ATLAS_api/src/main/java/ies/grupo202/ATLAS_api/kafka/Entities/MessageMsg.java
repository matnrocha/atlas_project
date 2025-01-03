package ies.grupo202.ATLAS_api.kafka.Entities;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageMsg {
    private int messageLog_id;
    private int sender_id;
    private String message;
    private LocalDateTime timestamp;

}
