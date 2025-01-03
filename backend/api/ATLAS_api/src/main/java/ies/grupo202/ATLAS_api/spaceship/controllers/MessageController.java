package ies.grupo202.ATLAS_api.spaceship.controllers;

import ies.grupo202.ATLAS_api.spaceship.entities.Message;
import ies.grupo202.ATLAS_api.spaceship.entities.MessageLog;
import ies.grupo202.ATLAS_api.spaceship.services.MessageLogService;
import ies.grupo202.ATLAS_api.spaceship.services.MessageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final MessageService messageService;
    private final MessageLogService messageLogService;

    // ------------------- Message Endpoints -------------------

    @Operation(summary = "Create a new message")
    @ApiResponse(responseCode = "201", description = "Message created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("{id}/message")
    public ResponseEntity<Message> createMessage(@PathVariable Long id,
            @RequestBody Message message) {
        Message createdMessage = messageService.createMessage(id, message);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a message by id")
    @ApiResponse(responseCode = "200", description = "Message found")
    @ApiResponse(responseCode = "404", description = "Message not found")
    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        if (message == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @Operation(summary = "Delete a message by id")
    @ApiResponse(responseCode = "204", description = "Message deleted")
    @ApiResponse(responseCode = "404", description = "Message not found")
    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get latest message in a messageLog")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/message/latest")
    public ResponseEntity<Message> getLatestMessageByMessageLogId(@PathVariable Long id) {
        Message message = messageService.getLatestMessageByMessageLogId(id);
        return message != null ? new ResponseEntity<>(message, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Get all messages in a messageLog")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<Message>> getMessagesByMessageLogId(@PathVariable Long id) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get messages containing a substring, in a messageLog", description = "Given a substring, return all messages containing it")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/messages/{id}/search")
    public ResponseEntity<List<Message>> getMessagesByMessageLogIdContaining(@PathVariable Long id,
            @RequestParam String substring) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        messages.removeIf(message -> !message.getMessage().contains(substring));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get messages by sender, in a messageLog", description = "Given a sender, return all messages sent by it")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/messages/{id}/sender")
    public ResponseEntity<List<Message>> getMessagesByMessageLogIdAndSender(@PathVariable Long id,
            @RequestParam String sender) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        if (sender != null) {
            messages.removeIf(message -> message.getSender() == null || !message.getSender().getName().equals(sender));
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get messages by sender, containing a substring, in a messageLog", description = "Given a sender and a substring, return all messages sent by the sender containing the substring, inside the given message Log")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/messages/{id}/sender/search")
    public ResponseEntity<List<Message>> getMessagesByMessageLogIdAndSenderContaining(@PathVariable Long id,
            @RequestParam String sender, @RequestParam String substring) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        messages.removeIf(
                message -> message.getSender() == null || !message.getSender().getName().equals(sender)
                        || !message.getMessage().contains(substring));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get messages containing a substring", description = "Given a substring, return all messages containing it")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/messages/search")
    public ResponseEntity<List<Message>> getMessagesContaining(@RequestParam String substring) {
        List<Message> messages = messageService.getMessagesContaining(substring);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Same endpoints but global, not linked to a specific message log.
    // Useful for building reports and statistics, possibly.
    @Operation(summary = "Get messages by sender", description = "Given a sender, return all messages sent by it")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/messages/sender")
    public ResponseEntity<List<Message>> getMessagesBySender(@RequestParam String sender) {
        List<Message> messages = messageService.getMessagesBySenderName(sender);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get messages by sender, containing a substring", description = "Given a sender and a substring, return all messages sent by the sender containing the substring")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/messages/sender/search")
    public ResponseEntity<List<Message>> getMessagesBySenderContaining(@RequestParam String sender,
            @RequestParam String substring) {
        List<Message> messages = messageService.getMessagesBySenderName(sender);

        messages.removeIf(message -> !message.getMessage().contains(substring));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // ------------------- System Message Endpoints -------------------
    @Operation(summary = "Get system messages, for a given messageLog", description = "Return all messages sent by the system (sender is null)")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages/system")
    public ResponseEntity<List<Message>> getSystemMessagesByMessageLogId(@PathVariable Long id) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        messages.removeIf(message -> message.getSender() != null);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get the n latest system messages for a given messageLog", description = "Return the n latest messages sent by the system (sender is null)")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages/system/latest")
    public ResponseEntity<List<Message>> getLatestSystemMessagesByMessageLogId(@PathVariable Long id,
            @RequestParam int n) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        messages.removeIf(message -> message.getSender() != null);
        if (messages.size() > n) {
            messages = messages.subList(messages.size() - n, messages.size());
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    // ------------------------------------------------------------

    // Same endpoints but working for the N latest messages in the system, linked to
    // a specific message log.
    @Operation(summary = "Get N latest messages in a messageLog")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages/latest")
    public ResponseEntity<List<Message>> getLatestMessagesByMessageLogId(@PathVariable Long id, @RequestParam int n) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        // If there are more than n messages, return the last n
        if (messages.size() > n) {
            messages = messages.subList(messages.size() - n, messages.size());
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get N latest messages containing a substring, in a messageLog", description = "Given a substring, return the N latest messages containing it")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages/latest/search")
    public ResponseEntity<List<Message>> getLatestMessagesByMessageLogIdContaining(@PathVariable Long id,
            @RequestParam String substring, @RequestParam int n) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        String lowerCaseSubstring = substring.toLowerCase();
        messages.removeIf(message -> !message.getMessage().toLowerCase().contains(lowerCaseSubstring));
        if (messages.size() > n) {
            messages = messages.subList(messages.size() - n, messages.size());
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get N latest messages by sender, in a messageLog", description = "Given a sender, return the N latest messages sent by it")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages/latest/sender")
    public ResponseEntity<List<Message>> getLatestMessagesByMessageLogIdAndSender(@PathVariable Long id,
            @RequestParam String sender, @RequestParam int n) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        if (sender != null) {
            messages.removeIf(message -> message.getSender() == null || !message.getSender().getName().equals(sender));
        }
        if (messages.size() > n) {
            messages = messages.subList(messages.size() - n, messages.size());
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Get N latest messages by sender, containing a substring, in a messageLog", description = "Given a sender and a substring, return the N latest messages sent by the sender containing the substring, inside the given message Log")
    @ApiResponse(responseCode = "200", description = "Messages found")
    @ApiResponse(responseCode = "404", description = "Messages not found")
    @GetMapping("/{id}/messages/latest/sender/search")
    public ResponseEntity<List<Message>> getLatestMessagesByMessageLogIdAndSenderContaining(@PathVariable Long id,
            @RequestParam String sender, @RequestParam String substring, @RequestParam int n) {
        List<Message> messages = messageService.getMessagesByMessageLogId(id);
        String lowerCaseSubstring = substring.toLowerCase();
        messages.removeIf(
                message -> message.getSender() == null || !message.getSender().getName().equals(sender)
                        || !message.getMessage().toLowerCase().contains(lowerCaseSubstring));
        if (messages.size() > n) {
            messages = messages.subList(messages.size() - n, messages.size());
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // ------------------- MessageLog Endpoints -------------------

    @Operation(summary = "Create a new message log")
    @ApiResponse(responseCode = "201", description = "Message log created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping("{id}/log")
    public ResponseEntity<MessageLog> createMessageLog(@PathVariable Long id, @RequestBody MessageLog messageLog) {
        MessageLog createdLog = messageLogService.createMessageLog(id, messageLog);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a message log by id")
    @ApiResponse(responseCode = "200", description = "Message log found")
    @ApiResponse(responseCode = "404", description = "Message log not found")
    @GetMapping("/log/{id}")
    public ResponseEntity<MessageLog> getMessageLogById(@PathVariable Long id) {
        MessageLog messageLog = messageLogService.getMessageLogById(id);
        return messageLog != null ? new ResponseEntity<>(messageLog, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Get all message logs")
    @ApiResponse(responseCode = "200", description = "Message logs found")
    @ApiResponse(responseCode = "404", description = "Message logs not found")
    @GetMapping("/logs")
    public ResponseEntity<List<MessageLog>> getAllMessageLogs() {
        List<MessageLog> messageLogs = messageLogService.getAllMessageLogs();
        return new ResponseEntity<>(messageLogs, HttpStatus.OK);
    }

    @Operation(summary = "Delete a message log by id")
    @ApiResponse(responseCode = "200", description = "Message log deleted")
    @ApiResponse(responseCode = "404", description = "Message log not found")
    @DeleteMapping("/log/{id}")
    public ResponseEntity<Void> deleteMessageLog(@PathVariable Long id) {
        messageLogService.deleteMessageLog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get message logs by Spaceship id", description = "Given an id, return all message logs attached to it")
    @ApiResponse(responseCode = "200", description = "Message logs found")
    @ApiResponse(responseCode = "404", description = "Message logs not found")
    @GetMapping("/logs/spaceship")
    public ResponseEntity<List<MessageLog>> getMessageLogsBySpaceshipId(@RequestParam Long id) {
        List<MessageLog> messageLogs = messageLogService.getMessageLogsBySpaceshipId(id);
        return new ResponseEntity<>(messageLogs, HttpStatus.OK);
    }
}
