package com.merchio.controller;

import com.merchio.ai.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class ChatRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;

    private String sessionId;

    private List<ChatMessage> conversationHistory;
}