package com.poly.model;

import lombok.Data;

@Data
public class ChatMessage {
    private ChatMessageType type;
    private String from;
    private String content;    // content = message hoặc danh sách user
}
