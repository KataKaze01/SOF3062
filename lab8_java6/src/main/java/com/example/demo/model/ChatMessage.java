package com.example.demo.model;

import lombok.*;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private List<String> onlineUsers; // Dùng cho Bài 2

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}