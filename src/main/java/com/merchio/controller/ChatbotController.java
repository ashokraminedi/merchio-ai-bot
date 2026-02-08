package com.merchio.controller;

import com.merchio.ai.ChatMessage;
import com.merchio.service.ChatbotResponse;
import com.merchio.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<ChatbotResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        log.info("📨 Received message from session: {}", request.getSessionId());

        ChatbotResponse response = chatbotService.processMessage(
                request.getMessage(),
                request.getConversationHistory()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse(
                "Merchio chatbot is running",
                "healthy",
                "0.0.1"
        ));
    }

    record HealthResponse(String message, String status, String version) {}
}