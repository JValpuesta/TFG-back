package com.valpuestajorge.conecta4.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Habilita el uso de "/topic" como prefijo para las salidas
        registry.setApplicationDestinationPrefixes("/app"); // Habilita el uso de "/app" como prefijo para las solicitudes entrantes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("ws").setAllowedOrigins("http://localhost:4200/", "http://localhost:4200",
                "http://localhost:4201/", "http://localhost:4201").withSockJS();
    }
}