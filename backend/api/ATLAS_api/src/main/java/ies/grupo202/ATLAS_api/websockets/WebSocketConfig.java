package ies.grupo202.ATLAS_api.websockets;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
// import java.util.Arrays;
// import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // @Value("${cors.allowed-origins}")
    // private String stringCors;
    // 
    // String[] corsOriginsList =stringCors.split(","); 

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Broker destination prefix
        config.setApplicationDestinationPrefixes("/app"); // App destination prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // WebSocket endpoint
                .setAllowedOrigins("http://localhost" ) // Allow your React app's origin
                // .setAllowedOrigins("*") // Allow your React app's origin
                .withSockJS(); // Enable SockJS fallback
    }
}
