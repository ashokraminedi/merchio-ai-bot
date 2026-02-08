package com.merchio.ai;

import java.util.List;

/**
 * AI Provider interface for Merchio chatbot
 * Supports multiple AI providers: OpenAI, Claude, Azure
 */
public interface AIProvider {

    String generateResponse(String systemPrompt, String userPrompt);

    String generateResponse(String systemPrompt, List<ChatMessage> conversationHistory);

    String getProviderName();
}