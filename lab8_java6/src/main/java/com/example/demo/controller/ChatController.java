package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ChatController {

    // Dùng Set để lưu danh sách người đang online (tránh trùng lặp)
    public static Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

    // Xử lý tin nhắn chat bình thường
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    // Xử lý khi có người mới tham gia (JOIN) - Bài 2
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Thêm username vào session của WebSocket để dùng lúc disconnect
        String username = chatMessage.getSender();
        headerAccessor.getSessionAttributes().put("username", username);

        // Thêm vào danh sách online
        onlineUsers.add(username);

        // Gửi danh sách online mới nhất về cho mọi người
        chatMessage.setOnlineUsers(new ArrayList<>(onlineUsers));
        chatMessage.setContent("đã tham gia phòng chat!");
        return chatMessage;
    }
}