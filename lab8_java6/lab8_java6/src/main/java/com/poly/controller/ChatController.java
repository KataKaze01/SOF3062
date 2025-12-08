package com.poly.controller;

import com.poly.model.ChatMessage;
import com.poly.model.ChatMessageType;
import com.poly.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    UserSessionService userService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    // gửi tin nhắn
    @MessageMapping("/chat")
    public void chat(ChatMessage message) {
        message.setType(ChatMessageType.CHAT);
        messagingTemplate.convertAndSend("/topic/messages", message);
    }

    // thêm user
    @MessageMapping("/username")
    public void join(ChatMessage message) {

        var list = userService.addUser(message.getFrom());

        ChatMessage msg = new ChatMessage();
        msg.setType(ChatMessageType.JOIN);
        msg.setFrom(message.getFrom());
        msg.setContent(String.join(",", list));

        messagingTemplate.convertAndSend("/topic/users", msg);
    }

    // xử lý logout
    @MessageMapping("/leave")
    public void leave(ChatMessage message) {

        var list = userService.removeUser(message.getFrom());

        ChatMessage msg = new ChatMessage();
        msg.setType(ChatMessageType.LEAVE);
        msg.setFrom(message.getFrom());
        msg.setContent(String.join(",", list));

        messagingTemplate.convertAndSend("/topic/users", msg);
    }
}
