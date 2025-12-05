package com.example.demo.config;

import com.example.demo.controller.ChatController;
import com.example.demo.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // Lấy username từ session đã lưu lúc join
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            logger.info("User Disconnected: " + username);

            // Xóa khỏi danh sách online
            ChatController.onlineUsers.remove(username);

            // Tạo thông báo LEAVE và gửi cho mọi người
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .content("đã rời phòng chat!")
                    .onlineUsers(new ArrayList<>(ChatController.onlineUsers)) // Cập nhật danh sách mới
                    .build();

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}