package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Định nghĩa endpoint để client kết nối đến WebSocket
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Định nghĩa prefix cho các destination mà server sẽ gửi tin nhắn về cho client
        registry.enableSimpleBroker("/topic");
        // Định nghĩa prefix cho các destination mà client sẽ gửi lên server
        registry.setApplicationDestinationPrefixes("/app");
    }
}