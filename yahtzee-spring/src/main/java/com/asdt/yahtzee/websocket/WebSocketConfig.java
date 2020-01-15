package com.asdt.yahtzee.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private PlayerCatalog playerCatalog; 

    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gamews")
        .addInterceptors(new HttpHandshakeInterceptor(playerCatalog))
        .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue/");
        registry.setApplicationDestinationPrefixes("/app");
        /**
         * To receive messages in valid order:
         */
        registry.setPreservePublishOrder(true);
    }

    @EventListener
    public void onSocketConnected(SessionConnectedEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        logger.trace("[Connected] " + sha.getSessionId());
    }

    @EventListener
    public void onSocketDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        logger.trace("[Disonnected] " + sha.getSessionId());
        String httpSessionId = (String)sha.getSessionAttributes().get("sessionId");
        logger.trace(httpSessionId);
        playerCatalog.disconnected(httpSessionId);

        // PlayerCatalog.getInstance().print();

        //TODO: send disconnected user to players
    }

}