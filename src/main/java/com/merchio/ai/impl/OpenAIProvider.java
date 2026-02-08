package com.merchio.ai.impl;

import com.merchio.ai.AIProvider;
import com.merchio.ai.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
public class OpenAIProvider implements AIProvider {

    private final OpenAiService openAiService;
    private final String model;
    private final int maxTokens;

    public OpenAIProvider(
            @Value("${ai.openai.api-key}") String apiKey,
            @Value("${ai.openai.model}") String model,
            @Value("${ai.openai.max-tokens}") int maxTokens) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(30));
        this.model = model;
        this.maxTokens = maxTokens;
        log.info("🤖 Merchio initialized with OpenAI provider - Model: {}", model);
    }

    @Override
    public String generateResponse(String systemPrompt, String userPrompt) {
        List<ChatMessage> messages = List.of(
                ChatMessage.builder().role("system").content(systemPrompt).build(),
                ChatMessage.builder().role("user").content(userPrompt).build()
        );
        return generateResponse(systemPrompt, messages);
    }

    @Override
    public String generateResponse(String systemPrompt, List<ChatMessage> conversationHistory) {
        try {
            List<com.theokanning.openai.completion.chat.ChatMessage> openAiMessages =
                    conversationHistory.stream()
                            .map(msg -> new com.theokanning.openai.completion.chat.ChatMessage(
                                    msg.getRole(), msg.getContent()))
                            .collect(Collectors.toList());

            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(openAiMessages)
                    .maxTokens(maxTokens)
                    .temperature(0.7)
                    .build();

            ChatCompletionResult result = openAiService.createChatCompletion(request);
            String response = result.getChoices().get(0).getMessage().getContent();

            log.debug("✅ OpenAI response generated successfully");
            return response;

        } catch (Exception e) {
            log.error("❌ Error calling OpenAI API: {}", e.getMessage());
            throw new RuntimeException("Failed to generate AI response from OpenAI", e);
        }
    }

    @Override
    public String getProviderName() {
        return "OpenAI";
    }
}